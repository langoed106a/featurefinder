package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.util.feature.FeatureFunction;
import app.util.feature.Section;
import app.util.feature.General;
import app.util.feature.WordStorage;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.regex.Match;

public class RegexHandler {
      private static final Logger logger = LoggerFactory.getLogger(RegexHandler.class);
      private static String WILDCARD_SEARCH="manyword";
      private static String WILDCARD_REGEX="manyword(\"%1\")>*<token=\"%1\">";
      private TextRegularExpressionParser textRegularExpressionParser;
      private TextRegularExpression regularExpression;
      private FeatureFunction featureFunction;
      private TextBlock textBlock;
      private Boolean regexError;
     
 public RegexHandler(FeatureFunction featureFunction, WordStorage wordStorage) {
      regexError=false;
      this.featureFunction = featureFunction;
      this.textBlock = new TextBlock();
      textRegularExpressionParser = new TextRegularExpressionParser(featureFunction, textBlock);
 }
 
 public Boolean parseRegex(String regex) {
      Boolean parsedOkay = true;
      String refinedRegex = preProcess(regex);
      try {
           regularExpression = textRegularExpressionParser.process(refinedRegex);
      } catch (Exception exception) {
            exception.printStackTrace();
            regexError = true;
            parsedOkay = false;
            logger.error("Failed to parse regex:"+refinedRegex);
      }
    return parsedOkay;
 }
 
 public Integer matchescount(Section section) {
     Integer finds=0;
     List<Match<WordToken>> matches=null;
     this.textBlock.setSection(section);
     this.textBlock.setTextBlockExpression(new TextBlockExpression());
     if (!regexError) {
          matches = regularExpression.findAll(section.getText());
          finds = matches.size();
     }      
   return finds;
 }
 
 public List<String> matchestext(Section section) {
 Integer finds=0;
 List<Match<WordToken>> matches=null;
 List<String> groupList = new ArrayList<>();
 this.textBlock.setSection(section);
 this.textBlock.setTextBlockExpression(new TextBlockExpression());
 if (!regexError) {
     matches = regularExpression.findAll(section.getText());
     for (Match<WordToken> match:matches) {
      groupList.add(match.startIndex()+":"+match.endIndex());
     }
 }    
return groupList;
 }  
 
 
 /* this regular expression engine is greedy */
 
 private String preProcess(String regex) {
       Integer position, startIndex=0, endIndex=0, index, wildcardLength = WILDCARD_SEARCH.length();
       String refinedRegex = regex, wildCardRegex="", part="", subStr="", word="";
       List<String> parameters=null;
       Boolean finish = false;
       if (regex.contains(WILDCARD_SEARCH)) {
             position=regex.indexOf(WILDCARD_SEARCH);
             if (position>-1) {
                  startIndex = position;
                  part=regex.substring(position+wildcardLength, regex.length());
                  if (part.startsWith("(")) {
                      position = part.indexOf(")>");
                      if (position>-1) {
                           endIndex=position+startIndex+wildcardLength+2;
                           subStr = part.substring(1,position);
                           parameters = General.getListParameters(subStr);
                           if (parameters.size()==1) {
                                word = parameters.get(0);
                                wildCardRegex=WILDCARD_REGEX.replace("%1", word);
                                if (endIndex<refinedRegex.length()) {
                                    refinedRegex = refinedRegex.substring(0,startIndex)+wildCardRegex+refinedRegex.substring(endIndex,refinedRegex.length());
                                } else {
                                    refinedRegex = refinedRegex.substring(0,startIndex)+wildCardRegex;
                                }
                          }
                  } 
              }
           }
      }  
    return refinedRegex;
 }
 
}