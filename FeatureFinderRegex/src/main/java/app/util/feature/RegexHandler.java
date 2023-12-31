package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.util.feature.FeatureFunction;
import app.util.feature.TextDocument;
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
      try {
           regularExpression = textRegularExpressionParser.process(regex);
      } catch (Exception exception) {
            exception.printStackTrace();
            regexError = true;
            parsedOkay = false;
            logger.error("Failed to parse regex:"+regex);
      }
    return parsedOkay;
 }
 
 public Integer matchescount(TextDocument document, Integer lineIndex) {
     Integer finds=0;
     List<Match<WordToken>> matches=null;
     this.textBlock.setTextDocument(document);
     this.textBlock.setTextBlockExpression(new TextBlockExpression());
     if (!regexError) {
          matches = regularExpression.findAll(document.getSentenceAtIndex(lineIndex));
          finds = matches.size();
     }      
   return finds;
 }
 
 public List<String> matchestext(TextDocument document, Integer lineIndex) {
     Integer finds=0;
     List<Match<WordToken>> matches=null;
     List<String> groupList = new ArrayList<>();
     this.textBlock.setTextDocument(document);
     this.textBlock.setTextBlockExpression(new TextBlockExpression());
     if (!regexError) {
         matches = regularExpression.findAll(document.getSentenceAtIndex(lineIndex));
         for (Match<WordToken> match:matches) {
             groupList.add(match.startIndex()+":"+match.endIndex());
         }
     }    
     return groupList;
 }  
 
}