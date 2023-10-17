package app.util.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import app.util.feature.FeatureFunction;
import app.util.feature.RegexDocument;
import app.util.feature.RegexDocument;
import app.util.feature.WordStorage;
import app.util.feature.WordToken;

public class Matcher {
private RegexHandler regexHandler;
private RegexDocument regexDocument;
private ContractFunction contractFunction;
private WordStorage wordStorage;
private Boolean parseRegex;

public Matcher(RegexDocument regexDocument, FeatureFunction featureFunction, WordStorage wordStorage, ContractFunction contractFunction) {
     this.regexHandler = new RegexHandler(featureFunction, wordStorage);
     this.regexDocument = regexDocument;
     this.contractFunction = contractFunction;
     this.parseRegex = regexHandler.parseRegex(regexDocument.getRegex());
     this.wordStorage = wordStorage;
}

public Integer matchcount(TextDocument textDocument) throws ParseRegexException { 
  Boolean preOkay=true, postOkay=true;
  TextDocument sentenceSection = null; 
  Integer match = 0, sentenceCount = 0, index = 0, partMatch = 0; 
  Properties textProperties=null;
  List<WordToken> sentence = null; 
  if (parseRegex) { 
      textProperties = new Properties();
      if (regexDocument.getGranularity().equalsIgnoreCase("sentence")) { 
             sentenceCount = textDocument.getSentenceCount(); 
             textProperties.setProperty("$sentences",String.valueOf(sentenceCount));
             while (index<sentenceCount) { 
                sentence = textDocument.getSentenceAtIndex(index); 
                textProperties.setProperty("$words",String.valueOf(sentence.size())); 
                sentenceSection = this.createSingleSentence(sentence);  
                if (contractFunction.preconditionExists(regexDocument)) { 
                   preOkay = contractFunction.doPrecondition(regexDocument, sentenceSection); 
                   if (preOkay) { 
                          partMatch = this.regexHandler.matchescount(sentenceSection); 
                   } else { 
                          partMatch = 0; 
                   } 
                   match = match + partMatch; 
                } else { 
                        partMatch = this.regexHandler.matchescount(sentenceSection); 
                         match = match + partMatch; 
                }          
                index++; 
             } 
      } else if (regexDocument.getGranularity().equalsIgnoreCase("text")) {
          sentence = textDocument.getSentenceAtIndex(index); 
          textProperties.setProperty("$words",String.valueOf(sentence.size()));       
          textProperties.setProperty("$sentences",String.valueOf(textDocument.getSentenceCount()));
          if (contractFunction.preconditionExists(regexDocument)) { 
                   preOkay = contractFunction.doPrecondition(regexDocument, textDocument); 
                   if (preOkay) { 
                          match = this.regexHandler.matchescount(textDocument); 
                   } else { 
                          match=0; 
                   } 
          } else {         
                    match = this.regexHandler.matchescount(textDocument);  
          }       
      } 
      if (contractFunction.postconditionExists(regexDocument)) { 
         textProperties.setProperty("$matches",String.valueOf(match));
         postOkay = contractFunction.doPostcondition(regexDocument, textProperties); 
         if (postOkay) { 
                match=1; 
         } else { 
                match=0; 
         } 
      } 
  } else { 
              throw new ParseRegexException("Error in Regex"); 
  } 
  return match; 
}

public List<String> matchtext(TextDocument textDocument) throws ParseRegexException {
    Integer match = 0, sentenceCount = 0, index = 0, partMatch = 0, offset=0, start=0, end=0;
    List<String> groups = null;
    List<String> totalGroups = new ArrayList<>();
    List<WordToken> sentence=null;
    TextDocument sentenceSection=null;
    String group="";
    String[] parts=null;
    if (parseRegex) {
        if (regexDocument.getGranularity().equalsIgnoreCase("sentence")) {
           sentenceCount = textDocument.getSentenceCount();
           while (index<sentenceCount) {
               sentence = textDocument.getSentenceAtIndex(index);
               sentenceSection = this.createSingleSentence(sentence);
               groups = regexHandler.matchestext(sentenceSection);
               for (String line:groups) {
                   parts = line.split(":");
                   start = Integer.valueOf(parts[0]);
                   end = Integer.valueOf(parts[1]);
                   start = start + offset;
                   end = end + offset;
                   group = String.valueOf(start)+":"+String.valueOf(end);
                   totalGroups.add(group);
               }
              offset = offset + sentence.size();
              index++;
           }
        } else if (regexDocument.getGranularity().equalsIgnoreCase("text")) {
                 totalGroups = regexHandler.matchestext(textDocument);
        }
    } else {
            throw new ParseRegexException("Error in Regex");
    }
  return totalGroups;
 }

 private TextDocument createSingleSentence(List<WordToken> sentence) {
       List<WordToken> singleSentence = new ArrayList<>();
       TextDocument textDocument = new TextDocument();
       WordToken wordToken = null;
       for (WordToken item:sentence) {
            wordToken = new WordToken(item.getToken(), item.getLemma(), item.getPostag(), item.getDependency(), item.getIndex(), 0);
            singleSentence.add(wordToken);
       }
       textDocument.addSentence(singleSentence);
     return textDocument;
  }
}
