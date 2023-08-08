package app.util.feature;

import java.util.ArrayList;
import java.util.List;
    
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
    
public class Section {
    private List<List<WordToken>> sentenceList;
    private List<String> matches;
    private Integer currentWordIndex;
    private Integer currentSentenceIndex;
    private Integer wordLimit;
       
    public Section() {
      sentenceList = new ArrayList<>();
      matches = new ArrayList<>();
      currentSentenceIndex=0;
      currentWordIndex=0;
      wordLimit=0;
    }
       
    public void addSentence(List<WordToken> sentence) {
      sentenceList.add(sentence);
    }
       
    public void addMatches(List<String> matchItems) {
       for (String item:matchItems) {
          matches.add(item);
       }
    }
       
    public void setMatches(List<String> matches) {
      this.matches = matches;
    }
       
    public List<String> getMatches() {
      return this.matches;
    }
       
    public Integer getCurrentSentenceIndex() {
      return currentSentenceIndex;
    }
       
    public void setCurrentWordIndex(Integer wordIndex) {
      this.currentWordIndex=wordIndex;
    }
       
    public Integer getWordCount() {
      Integer numberOfWords = 0;
      for (List<WordToken> someSentence: sentenceList) {
          numberOfWords = numberOfWords + someSentence.size();
      }
      return numberOfWords;
    }
       
    public Integer getWordLimit() {
      Integer numberOfWords = sentenceList.get(currentSentenceIndex).size();
      return numberOfWords;
    }
       
    public Integer getStartWordIndex() {
      return sentenceList.get(currentSentenceIndex).get(0).getIndex();
    }
       
    public Integer getCurrentWordIndex() {
      return currentSentenceIndex;
    }
       
    public Integer getSentenceCount() {
      return sentenceList.size();
    }
       
    public void setCurrentSentence(Integer sentenceIndex) {
      this.currentWordIndex=sentenceIndex;
    }
       
    public void incrementCurrentWordIndex() {
      this.currentWordIndex++;
    }
       
    public void toSingleSentence() {
      List<List<WordToken>> temporaryList = new ArrayList<>();
      List<WordToken> temporarySentence = new ArrayList<>();
      for (List<WordToken> sentence:sentenceList) {
          for (WordToken wordToken:sentence) {
             temporarySentence.add(wordToken);
          }    
      }
      temporaryList.add(temporarySentence);
      sentenceList=temporaryList;
      this.currentSentenceIndex=0;
    }
       
    public List<WordToken> getCurrentSentence() {
      List<WordToken> sentence=null;
      if (currentSentenceIndex<sentenceList.size()) {
          sentence = sentenceList.get(currentSentenceIndex);
      }
      return sentence;
    }
       
       public List<WordToken> getText() {
          List<WordToken> text=new ArrayList<>();
          List<WordToken> sentence=null;
          for (int i=0;i<sentenceList.size();i++) {
             sentence = sentenceList.get(i);
             for (WordToken wordToken:sentence) {
                text.add(wordToken);
             }   
          }
        return text;
       }
       
       public List<WordToken> getSentenceAtIndex(Integer index) {
          List<WordToken> sentence=null;
          if ((index>=0) && (index<sentenceList.size())) {
             sentence = sentenceList.get(index);
          }
        return sentence;
       }
       
       public List<WordToken> cloneToOneSentence() {
          Integer index = 0;
          WordToken itemToken = null;
          List<WordToken> cloneSentence = null, sentence = null;
          cloneSentence = new ArrayList<>();
          for (int i=0;i<sentenceList.size();i++) {
              sentence = sentenceList.get(i);
              for (WordToken wordToken:sentence) {
                   itemToken = new WordToken(wordToken.getToken(), wordToken.getLemma(), wordToken.getPostag(), wordToken.getDependency(), index, 0);
                   index = index + 1;
                   cloneSentence.add(itemToken);
              }
          }
        return cloneSentence;
       }
    
    public String toJson() {
      String jsonString="[", jsonLine="";
      Integer count=0;
      for (List<WordToken> line:sentenceList) {
           jsonLine="{\"linenumber\":\"" + String.valueOf(count) + "\",";
           jsonLine=jsonLine + "\"line\":[";
           for (WordToken wordToken:line) {
               jsonLine = jsonLine + wordToken.toJson();
               jsonLine = jsonLine + ",";
           }
           if (jsonLine.endsWith(",")) {
               jsonLine = jsonLine.substring(0, jsonLine.length()-1);
           }    
           jsonLine = jsonLine + "]";
           jsonLine = jsonLine + "},";
           jsonString = jsonString + jsonLine;
           count=count+1;
      }
      if (jsonString.length()>1) {
         jsonString = jsonString.substring(0, jsonString.length()-1);  
      }
      jsonString = jsonString + "]";
      return jsonString;
    }
    
    public void fromJson(String jsonString) {
      Integer count=0, sentence=1;
      JSONObject jsonObject = null;
      JSONArray jsonArray = null, lineArray = null;
      JSONParser parser = null;
      List<WordToken> wordTokenList = null;
      Object element = null, object = null, item = null;
      String jsonLine="", lineNumber="";
      WordToken wordToken = null;
      parser = new JSONParser();
      try {
             element = parser.parse(jsonString);
      } catch (Exception exception) {
           exception.printStackTrace();
           element = null;
      }  
      if (element instanceof JSONArray) {
          this.wordLimit=0;
          jsonArray = (JSONArray)element;
          this.sentenceList = new ArrayList<>();
          for (int i = 0; i <jsonArray.size(); i++) {
                   object = jsonArray.get(i);
                   if (object instanceof JSONObject) {
                       jsonObject = ((JSONObject) object);
                       lineNumber = (String)jsonObject.get("linenumber");
                       wordTokenList = new ArrayList<>();
                       if (lineNumber != null) {
                            lineArray = (JSONArray)jsonObject.get("line");
                            for (int k = 0; k<lineArray.size(); k++) {
                               item = lineArray.get(k);
                               wordToken = new WordToken();
                               wordToken.fromJson(((JSONObject) item));
                               wordTokenList.add(wordToken);
                               this.wordLimit=this.wordLimit+1;
                            }
                       }
                       sentence=sentence + 1;
                       this.sentenceList.add(wordTokenList);
                   }
          }
      }
     }
    }