package app.util.feature;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import app.util.feature.Document;
import app.util.feature.RegexDocument;
import app.util.feature.FeatureFunction;
import app.util.feature.FunctionCallback;
import app.util.feature.TextDocument;
import app.util.feature.General;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.logic.Expression.Arg;
import edu.washington.cs.knowitall.regex.Match;

public class TextLogicBaseExpression extends Arg<WordToken> implements FunctionCallback {
  private static String[] PART_LIST = {"firsttoken","FIRSTTOKEN","firstpostag","FIRSTPOSTAG","postag","POSTAG","token","TOKEN","lemma","LEMMA","type","TYPE","text","TEXT","phrase","PHRASE"};
  private static Integer FIRST_TOKEN_INDEX=0;
  private CustomRegularExpressionParser customRegularExpressionParser;
  private List<Match<WordToken>> customMatches;
  private String part;
  private String value;
  private String valueType;
  private FeatureFunction featureFunction;
  private TextBlock textBlock;
       
  public TextLogicBaseExpression(String regex, FeatureFunction featureFunction, TextBlock textBlock) {
    this.featureFunction = featureFunction;
    this.textBlock = textBlock;
    this.part=null;
    this.valueType=null;
    String tag="";
    customMatches = new ArrayList<>();
    if ((regex!=null) && (regex.startsWith("<")) && (regex.endsWith(">"))) {
        regex = regex.substring(1, regex.length());
        regex = regex.substring(0, regex.length()-1);  
    }
    tag = this.getPart(regex);
    if (tag.length()>0) {
           this.part = tag.toLowerCase();
           this.value = regex.substring(part.length()+1,regex.length());
           this.valueType = this.getValueType(value);
    }
    customRegularExpressionParser = new CustomRegularExpressionParser(featureFunction, textBlock);
  }
 
  public boolean apply(WordToken wordToken) {
    Boolean found = false;
    Integer index = 0, position = 0;
    WordToken tempToken = null;
    if ((valueType!=null) && (part!=null)) {
        if (part.equalsIgnoreCase("text")) {
             found = this.checkText(part, value, valueType, wordToken, textBlock);
        } else if (part.equalsIgnoreCase("firsttoken")) {
                 tempToken = General.getWordToken(FIRST_TOKEN_INDEX, textBlock.getTextDocument().getSentenceAtIndex(wordToken.getSentence()));
                 found=applyWordToken("token", value, valueType, tempToken, textBlock);
        } else if (part.equalsIgnoreCase("firstpostag")) {
                 tempToken = General.getWordToken(FIRST_TOKEN_INDEX, textBlock.getTextDocument().getSentenceAtIndex(wordToken.getSentence()));
                 found=applyWordToken("postag", value, valueType, tempToken, textBlock);
        } else {  
                found=applyWordToken(part, value, valueType, wordToken, textBlock);
        }  
   }
  return found;
  }
 
  private Boolean applyWordToken(String part, String value, String valueType, WordToken wordToken, TextBlock textBlock) {
    Boolean found = false;
    switch (valueType) {
        case "string": {found = checkString(part, value, wordToken, textBlock);}; break;
        case "name": {found = checkName(part, value, wordToken);}; break;
        case "list": {found = checkList(part, value, wordToken, textBlock);}; break;
        case "function": {found= checkFunction(part, valueType, value, wordToken, textBlock);}; break;
        case "predefinedlist": {found= checkPreDefinedList(part, valueType, value, wordToken, textBlock.getTextDocument());}; break;
        case "predefinedregex": {found= checkPreDefinedRegex(part, valueType, value, wordToken, textBlock.getTextDocument());}; break;
    }
    return found;
  }
 
  private String getPart(String regex) {
    boolean found = false;
    Integer index=0;
    String part="", str="", nextStr="";
    while ((!found) && (index<PART_LIST.length)) {
        str = PART_LIST[index];
        if (regex.startsWith(str)) {
            nextStr = regex.substring(str.length(), str.length()+1);
            if (nextStr.equalsIgnoreCase("=")) {
                found = true;
                part = str.toLowerCase();
            }  
       }
      index = index+1;
    }
   return part;
  };
 
  private String getValueType(String value) {
    String type = "";
    type = isNumber(value);
    if (type.length()==0) {
        type = isString(value);
        if (type.length()==0) {
            type = isFunction(value);
            if (type.length()==0) {
                type = isList(value);
                if (type.length()==0) {
                    type = isPredefinedList(value);
                    if (type.length()==0) {
                        type = isPredefinedRegex(value);
                    }
                }
            }
        }
    }
   return type;
  };

  private String isNumber(String value) {
    String type="";
    Character digits[]= {'0','1','2','3','4','5','6','7','8','9'};
    char chr;
    List<Character> digitsList = Arrays.asList(digits);
    boolean okay=true;
    for (int i=0;i<value.length();i++) {
        chr = value.charAt(i);
        if (!digitsList.contains(chr)) {
            okay = false;
        }
     }
    if (okay) {
        type="number";
    }
    return type;
  }
 
  private String isString(String value) {
      String type="";
      Character singleQuotes[] = {'\'','\''};
      Character doubleQuotes[]= {'"','"'};
      List<Character> singles=Arrays.asList(singleQuotes);
      List<Character> doubles=Arrays.asList(doubleQuotes);
      Character start,end;
      boolean okay=false;
      start = value.charAt(0);
      end = value.charAt(value.length()-1);
      if ((singles.contains(start)) && (singles.contains(end))) {
            okay=true;
      } else  if ((doubles.contains(start)) && (doubles.contains(end))) {
            okay=true;
      }
      if (okay) { 
            type="string";
      }
    return type;
  }
 
  private String isList(String value) {
     String type="";
     Character start,end;
     boolean okay=false;
     start = value.charAt(0);
     end = value.charAt(value.length()-1);
     if ((start=='[') && (end==']')) {
          okay=true;
     }
     if (okay) {
        type="list";
     }
     return type;
  }
 
  private String isFunction(String value) {
      String type="";
      Integer location=0;
      String functionName="",functionType="", end="";
      location = value.indexOf('(');
      if (location>0) {
           functionName=value.substring(0, location);
           functionType=this.isName(functionName);
           if (functionType.equalsIgnoreCase("name")) {
                end = value.substring(value.length()-1,value.length());
                if (end.equalsIgnoreCase(")")) {
                   type="function";
                }
            }
     }
    return type;
  }

  private String isPredefinedList(String value) {
      Boolean exists = false;
      String type="", prefix="";
      if (value.length()>0) {
        prefix = value.substring(0,1);
        if (prefix.equalsIgnoreCase("$")) {
           value = value.substring(1, value.length());
           exists = featureFunction.isPredefinedList(value);
           if (exists) {
            type="predefinedlist";
           }
        }
     }
    return type;
  }

  private String isPredefinedRegex(String value) {
      Boolean exists = false;
      String type="", prefix="";
      if (value.length()>0) {
        prefix = value.substring(0,1);
        if (prefix.equalsIgnoreCase("$")) {
           value = value.substring(1, value.length());
           exists = featureFunction.isPredefinedRegex(value);
           if (exists) {
            type="predefinedregex";
           }
        }
     }
    return type;
  }
 
  private String isName(String value) {
     String type="";
     Character lowercase[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
     Character number[]= {'0','1','2','3','4','5','6','7','8','9'};
     Character symbol[]= {'_'};
     Character uppercase[]= {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
     List<Character> validLower=Arrays.asList(lowercase);
     List<Character> validUpper=Arrays.asList(uppercase);
     List<Character> validNumber=Arrays.asList(number);
     List<Character> validSymbol=Arrays.asList(symbol);
     Character start,end;
     char chr;
     Integer count=0;
     boolean okay=true;
     for (int i=0;i<value.length();i++) {
         chr = value.charAt(i);
         if (validLower.contains(chr)) {
             count++;
         } else if (validUpper.contains(chr)) {
             count++;
         } else if ((i>0) && (validNumber.contains(chr))) {
             count++;
         } else if ((i>0) && (validSymbol.contains(chr))) {
             count++;
         }
     }
     if (count==value.length()) {
         type="name";
     }
    return type;
  }
 
  private String getName(String value) {
      String functionName="";
      Integer location = value.indexOf('(');
      if (location>0) {
          functionName=value.substring(0, location);
      }
    return functionName;
  }
 
  private Boolean checkString(String part, String value, WordToken wordToken, TextBlock textBlock) {
     boolean found=false, finish=false;
     Integer index=0, wordIndex=0, wordTokenIndex=0, sentenceIndex=0;
     String content="", word="", wordPart="";
     sentenceIndex = wordToken.getSentence();
     value = General.removeQuotes(value);
     sentenceIndex = wordToken.getSentence();
     found = General.theSame(part, wordToken, textBlock.getTextDocument().getSentenceAtIndex(sentenceIndex), value);
     return found;
      }
     
      private Boolean checkText(String part, String value, String valueType, WordToken wordToken, TextBlock textBlock) {
          Boolean found = false;
          Integer sentenceIndex=0;
          Integer wordIndex=0;
          TextBlockExpression textBlockExpression=null;
          wordIndex = wordToken.getIndex();
          sentenceIndex = wordToken.getSentence();
          if ((wordIndex==1) && (sentenceIndex==1)) {
              textBlockExpression = textBlock.getTextBlockExpression();
              found = textBlockExpression.apply(part,value,valueType,wordToken,textBlock.getTextDocument());
          }
         return found;
     }
     
  private Boolean checkList(String part, String value, WordToken wordToken, TextBlock textBlock) {
      boolean found=false, finish=false;
      Integer index=0, wordIndex=0, wordTokenIndex=0, sentenceIndex=0;
      String param="", word="";
      String[] words = null;
      List<String> params=null;
      List<WordToken> sentence=null;
      sentenceIndex = wordToken.getSentence();
      params = General.getListParameters(value);
      sentence = textBlock.getTextDocument().getSentenceAtIndex(sentenceIndex);
      while ((!found) && (index<params.size())) {
          param = params.get(index);
          found = General.theSame(part, wordToken, sentence, param);
          index++;
     }
    return found;
  }
     
      private Boolean checkName(String part, String value, WordToken wordToken) {
          boolean found=false;
          String content="";
          content = General.getValue(part, wordToken);
          if (value.equalsIgnoreCase(content)) {
                  found = true;
           }
         return found;
      }

     private Boolean checkFunction(String part, String valueType, String value, WordToken wordToken, TextBlock textBlock) {
         boolean found = false;
         String functionName = this.getName(value);
         found = featureFunction.doFunction(part,functionName,value,wordToken,textBlock.getTextDocument(), this);
         return found;
      }
  
     private Boolean checkPreDefinedList(String part, String valueType, String value, WordToken wordToken, TextDocument textDocument) {
          Boolean found=false;
          String functionName = "";
          if ((wordToken != null) && (valueType.equalsIgnoreCase("predefinedlist"))) {  
              functionName = value.substring(1, value.length());
              found = featureFunction.doFunction(part,functionName,value,wordToken,textDocument, this);
          }    
        return found;
       }

   @Override
   public Boolean doFunction(String part, String valueType, String value, WordToken wordToken, TextDocument textDocument) {
        return this.checkPreDefinedRegex(part, valueType, value, wordToken, textDocument);
   }

  private Boolean checkPreDefinedRegex(String part, String valueType, String value, WordToken wordToken, TextDocument textDocument) {
      Boolean found=false, finished=false;
      CustomRegularExpression logicExpression;
      Document feature = null;
      RegexDocument regexDocument = null;
      String functionType="", definedRegex="", contents="", word="", wordToCheck;
      String[] wordList, items;
      List<WordToken> currentWordList = null, wordTokenList = null;
      List<String> groupList = new ArrayList<>();
      WordToken wordItem = null;
      Integer matchCount=0, index=0, wordIndex=0, position=0, start=0, end=0, finds=0, sentenceIndex=0;
      if ((wordToken != null) && (value.startsWith("$"))) {
          value = value.substring(1,value.length());
          feature = featureFunction.getPredefinedRegex(value);
          if ((feature!=null) && (feature.getType().equalsIgnoreCase("regex"))) {
              currentWordList = new ArrayList<>();
              wordIndex = wordToken.getIndex();
              sentenceIndex = wordToken.getSentence();
              wordTokenList = textDocument.getSentenceAtIndex(wordToken.getSentence());
              if (wordIndex==0) {
                  while (index<wordTokenList.size()) {
                        wordItem = wordTokenList.get(index);
                        position = wordItem.getIndex();
                        currentWordList.add(wordItem);
                        index = index + 1;
                  }
                 wordIndex = wordToken.getIndex();
                 regexDocument = new RegexDocument();
                 regexDocument.fromDocument(feature);
                 definedRegex = regexDocument.getRegex();
                 try {
                      definedRegex = URLDecoder.decode(definedRegex);
                 } catch (Exception exception) {
                    exception.printStackTrace();
                 }
                 logicExpression = customRegularExpressionParser.process(definedRegex);
                 customMatches = logicExpression.findAll(currentWordList);
              }
            for (Match match:customMatches) {
                 if ((wordIndex>=match.startIndex()) && (wordIndex<match.endIndex())) {
                     found = true;
                 }
            }
         }
      }
      return found;
   }
 
}