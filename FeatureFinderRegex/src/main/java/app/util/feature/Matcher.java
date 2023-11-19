package app.util.feature;

import java.net.URLDecoder;

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
     try {
           this.parseRegex = regexHandler.parseRegex(URLDecoder.decode(regexDocument.getRegex()));
     } catch (Exception exception) {
          exception.printStackTrace();
     }
     this.wordStorage = wordStorage;
}

public Integer matchcount(TextDocument textDocument) throws ParseRegexException { 
  Boolean preOkay=true, postOkay=true;
  Integer match = 0, sentenceCount = 0, index = 0, partMatch = 0; 
  Properties textProperties=null;
  Sentence sentence=null;
  TextDocument sentenceDocument=null;
  List<WordToken> line = null; 
  if (parseRegex) { 
      textProperties = new Properties();
      if (regexDocument.getGranularity().equalsIgnoreCase("sentence")) { 
             sentenceCount = textDocument.getSentenceCount(); 
             textProperties.setProperty("$sentences",String.valueOf(sentenceCount));
             while (index<sentenceCount) { 
                textProperties.setProperty("$words",String.valueOf(textDocument.getSentenceAtIndex(index+1).size()));  
                if (contractFunction.preconditionExists(regexDocument)) { 
                   preOkay = contractFunction.doPrecondition(regexDocument, textDocument); 
                   if (preOkay) { 
                          partMatch = this.regexHandler.matchescount(textDocument, index+1); 
                   } else { 
                          partMatch = 0; 
                   } 
                   match = match + partMatch; 
                } else {  
                        partMatch = this.regexHandler.matchescount(textDocument, index+1); 
                        match = match + partMatch; 
                }        
                index++; 
             } 
      } else if (regexDocument.getGranularity().equalsIgnoreCase("text")) {
          line = textDocument.getAsOneSentence();
          sentence = new Sentence();
          sentence.setTokenList(line); 
          sentenceDocument = new TextDocument();
          sentenceDocument.addSentence(sentence);
          textProperties.setProperty("$words",String.valueOf(line.size()));       
          textProperties.setProperty("$sentences",String.valueOf(sentenceDocument.getSentenceCount()));
          if (contractFunction.preconditionExists(regexDocument)) { 
                   preOkay = contractFunction.doPrecondition(regexDocument, sentenceDocument); 
                   if (preOkay) { 
                          match = this.regexHandler.matchescount(sentenceDocument, 1); 
                   } else { 
                          match=0; 
                   } 
          } else {         
                    match = this.regexHandler.matchescount(sentenceDocument, 1);  
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
    List<WordToken> tokenList=null;
    Sentence sentence=null;
    TextDocument sentenceDocument=null;
    String group="";
    String[] parts=null;
    if (parseRegex) {
        if (regexDocument.getGranularity().equalsIgnoreCase("sentence")) {
           sentenceCount = textDocument.getSentenceCount();
           while (index<sentenceCount) {
               groups = regexHandler.matchestext(textDocument, index+1);
               tokenList = textDocument.getSentenceAtIndex(index+1);
               for (String line:groups) {
                   parts = line.split(":");
                   start = Integer.valueOf(parts[0]);
                   end = Integer.valueOf(parts[1]);
                   start = start + offset;
                   end = end + offset;
                   group = String.valueOf(start)+":"+String.valueOf(end);
                   totalGroups.add(group);
               }
              offset = offset + tokenList.size();
              index++;
           }
        } else if (regexDocument.getGranularity().equalsIgnoreCase("text")) {
                 tokenList = textDocument.getAsOneSentence();
                 sentence = new Sentence();
                 sentence.setTokenList(tokenList);
                 sentenceDocument = new TextDocument();
                 sentenceDocument.addSentence(sentence);
                 totalGroups = regexHandler.matchestext(sentenceDocument, 1);
        }
    } else {
            throw new ParseRegexException("Error in Regex");
    }
  return totalGroups;
 }

}
