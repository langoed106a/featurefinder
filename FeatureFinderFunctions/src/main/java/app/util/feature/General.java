package app.util.feature;

import java.net.URLDecoder;

import java.util.ArrayList;
import java.util.List;

public class General {

    public static String removeQuotes(String content) {
        String newContent = content;
        if ((newContent!=null) && ((newContent.startsWith("'")) || (newContent.startsWith("\"")))) {
            newContent = newContent.substring(1, newContent.length());
        }
        if ((newContent!=null) && ((newContent.endsWith("'")) || (newContent.endsWith("\"")))) {
            newContent = newContent.substring(0, newContent.length()-1);
        }
      return newContent;
    }

   public static WordToken getWordToken(Integer tokenIndex, List<WordToken> tokenList) {
       WordToken wordToken=null, foundToken=null;
       Boolean finish = false;
       Integer index = 0;
       while ((index<tokenList.size()) && (!finish)) {
           wordToken = tokenList.get(index);
           if (wordToken.getIndex()==tokenIndex) {
               finish = true;
               foundToken = wordToken.clone();
           }
           index++;    
       }
       return foundToken;
   }

   public static List<String> getListParameters(String value) {
    List<String> parameters = new ArrayList<>();
    Boolean skip = false;
    String listParameters = value;
    String parameter="";
    String part="";
    
    if ((listParameters!=null ) && (listParameters.startsWith("["))) {
        listParameters = listParameters.substring(1, listParameters.length());
    }
    if ((listParameters!=null ) && (listParameters.endsWith("]"))) {
        listParameters = listParameters.substring(0, listParameters.length()-1);
    }
    if ((listParameters!=null ) && (listParameters.length()>0)) {
        skip = false;
        for (int i=0; i<listParameters.length();i++) {
             part = listParameters.substring(i, i+1);
             if ((part.equalsIgnoreCase(",")) && (!skip)) {
                 if (part.length()>0) {
                     parameter = removeQuotes(parameter);
                     parameters.add(parameter);
                     parameter="";
                 }
             } else {
                    parameter = parameter + part;         
                    if ((part.equalsIgnoreCase("'")) || (part.equalsIgnoreCase("\""))) {
                          skip = !skip;
                    }
             }
         }
         if (parameter.length()>0) {
             parameter = removeQuotes(parameter);
             parameters.add(parameter);
         }        
    }
    return parameters;
}

  public static List<String> getParameters(String name, String value) {
		List<String> parameters = new ArrayList<>();
		String functionParameters = "";
		if ((value.length()>0) && (value.startsWith(name))) {
			functionParameters = value.substring(name.length(), value.length());
			if (functionParameters.startsWith("(")) {
				functionParameters = functionParameters.substring(1,functionParameters.length());
			}
			if (functionParameters.endsWith(")")) {
				functionParameters = functionParameters.substring(0,functionParameters.length()-1);
			}
			   if (functionParameters.length() > 0) {
				     parameters = General.getListParameters(functionParameters);
				        }
		}
		return parameters;
	}

    public static List<String> getSingleParameter(String name, String value) {
		List<String> parameters = new ArrayList<>();
		String functionParameters = "";
		if ((value.length()>0) && (value.startsWith(name))) {
			functionParameters = value.substring(name.length(), value.length());
			if (functionParameters.startsWith("(")) {
				functionParameters = functionParameters.substring(1,functionParameters.length());
			}
			if (functionParameters.endsWith(")")) {
				functionParameters = functionParameters.substring(0,functionParameters.length()-1);
			}
			if (functionParameters.length() > 0) {
                     functionParameters = removeQuotes(functionParameters);
				     parameters.add(functionParameters);
			}
		}
		return parameters;
	} 

   public static String decode(String strCoded) {
       String strDecoded="";
       try {
             if (strCoded!=null) {
                strDecoded = URLDecoder.decode(strCoded, "UTF-8");
             }    
       } catch (Exception exception) {
           strDecoded="";
       }
     return strDecoded;
   }

   public static boolean isNumber(String value) {
       Integer number=0;
       Boolean isnumber=false;
       try {
             number = Integer.parseInt(value);
             isnumber=true;
       } catch (Exception exception) {
           isnumber=false;
       }
     return isnumber;
   }

   public static Boolean theSame(String part, WordToken wordToken, List<WordToken> tokenList, String wordToCheck) {
    WordToken itemToken=null, foundToken=null;
    Boolean theSame=false;
    Integer index = 0, count=0, wordIndex=0;
    String partWord="", word="";
    String[] words = null;
    if ((wordToCheck!=null) && (wordToCheck.length()>0)) {
        words = wordToCheck.split(" ");
        wordIndex = wordToken.getIndex();
        itemToken = General.getWordToken(wordIndex, tokenList);
        while ((index<words.length) && (itemToken!=null)) {
             word = General.getValue(part, itemToken);
             partWord = words[index];
             if (partWord.equalsIgnoreCase(word)) {
                count = count + 1;
             }
             wordIndex++;
             index++;
             itemToken = General.getWordToken(wordIndex, tokenList);
        }        
        if (count==words.length) {
            theSame = true;
        }    
     }
     return theSame;
}


    public static String getValue(String tag, WordToken wordToken) {
        String value="";
        if (tag==null) {
            tag="";
        }
        if (tag.equalsIgnoreCase("token")) {
            value = wordToken.getToken();
        } else  if (tag.equalsIgnoreCase("lemma")) {
            value = wordToken.getLemma();
        } else  if (tag.equalsIgnoreCase("postag")) {
            value = wordToken.getPostag();
        } else  if (tag.equalsIgnoreCase("type")) {
            value = wordToken.getDependency();
        } 
      return value;
    }
}