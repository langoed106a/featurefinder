package app.util.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import app.util.feature.FeatureFunction;
import app.util.feature.RegexFeature;
import app.util.feature.Section;
import app.util.feature.WordStorage;
import app.util.feature.WordToken;

public class Matcher {
private RegexHandler regexHandler;
private RegexFeature regexFeature;
private ContractFunction contractFunction;
private WordStorage wordStorage;
private Boolean parseRegex;

public Matcher(RegexFeature regexFeature, FeatureFunction featureFunction, WordStorage wordStorage, ContractFunction contractFunction) {
     this.regexHandler = new RegexHandler(featureFunction, wordStorage);
     this.regexFeature = regexFeature;
     this.contractFunction = contractFunction;
     this.parseRegex = regexHandler.parseRegex(regexFeature.getContents());
     this.wordStorage = wordStorage;
}

public Integer matchcount(Section section) throws ParseRegexException { 
  Boolean preOkay=true, postOkay=true; 
  Integer match = 0, sentenceCount = 0, index = 0, partMatch = 0; 
  Properties textProperties=null;
  List<WordToken> sentence = null; 
  Section sentenceSection = null; 
  if (parseRegex) { 
      textProperties = new Properties();
      if (regexFeature.getGranularity().equalsIgnoreCase("sentence")) { 
             sentenceCount = section.getSentenceCount(); 
             textProperties.setProperty("$sentences",String.valueOf(sentenceCount));
             while (index<sentenceCount) { 
                sentence = section.getSentenceAtIndex(index); 
                textProperties.setProperty("$words",String.valueOf(sentence.size())); 
                sentenceSection = this.createSingleSentence(sentence);  
                if (contractFunction.preconditionExists(regexFeature)) { 
                   preOkay = contractFunction.doPrecondition(regexFeature, sentenceSection); 
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
      } else if (regexFeature.getGranularity().equalsIgnoreCase("text")) {
          sentence = section.getSentenceAtIndex(index); 
          textProperties.setProperty("$words",String.valueOf(sentence.size()));       
          textProperties.setProperty("$sentences",String.valueOf(section.getSentenceCount()));
          if (contractFunction.preconditionExists(regexFeature)) { 
                   preOkay = contractFunction.doPrecondition(regexFeature, section); 
                   if (preOkay) { 
                          match = this.regexHandler.matchescount(section); 
                   } else { 
                          match=0; 
                   } 
          } else {         
                    match = this.regexHandler.matchescount(section);  
          }       
      } 
      if (contractFunction.postconditionExists(regexFeature)) { 
         textProperties.setProperty("$matches",String.valueOf(match));
         postOkay = contractFunction.doPostcondition(regexFeature, textProperties); 
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

public List<String> matchtext(Section section) throws ParseRegexException {
    Integer match = 0, sentenceCount = 0, index = 0, partMatch = 0, offset=0, start=0, end=0;
    List<String> groups = null;
    List<String> totalGroups = new ArrayList<>();
    List<WordToken> sentence=null;
    Section sentenceSection=null;
    String group="";
    String[] parts=null;
    if (parseRegex) {
        if (regexFeature.getGranularity().equalsIgnoreCase("sentence")) {
           sentenceCount = section.getSentenceCount();
           while (index<sentenceCount) {
               sentence = section.getSentenceAtIndex(index);
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
        } else if (regexFeature.getGranularity().equalsIgnoreCase("text")) {
                 totalGroups = regexHandler.matchestext(section);
        }
    } else {
            throw new ParseRegexException("Error in Regex");
    }
  return totalGroups;
 }

 private Section createSingleSentence(List<WordToken> sentence) {
       List<WordToken> singleSentence = new ArrayList<>();
       Section section = new Section();
       WordToken wordToken = null;
       for (WordToken item:sentence) {
            wordToken = new WordToken(item.getToken(), item.getLemma(), item.getPostag(), item.getDependency(), item.getIndex(), 0);
            singleSentence.add(wordToken);
       }
       section.addSentence(singleSentence);
     return section;
  }
}
