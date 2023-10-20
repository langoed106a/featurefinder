package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import app.util.feature.WordToken;
import app.util.feature.TextDocument;

public class TextDocument extends Document {
    private String language;
	List<Sentence> sentenceList;
	
    public TextDocument() {
		super();
		sentenceList = new ArrayList<>();
	}

    public TextDocument(String id, String name, String type, String language, String origin, String contents) {
    	this.id = id;
        this.name = name;
        this.origin = origin;
    	this.type = type;
    	this.language = language;
    	this.contents = contents;
    }
    
    public String getLanguage() {
    	return language;
    }
    
    public String getName() {
       return name;
    }  
    
    public String getContents() {
    	return contents;
    }

    public void setSentenceList(List<Sentence> sentenceList) {
        this.sentenceList = sentenceList;
    }

    public List<Sentence> getSentenceList() {
        return this.sentenceList = sentenceList;
    }
    
	public List<WordToken> getSentence(Integer index) {
		List<WordToken> line=null;
		Sentence sentence=null;
		if ((index>0) && (index<sentenceList.size())) {
			sentence = sentenceList.get(index);
			line = sentence.getSentenceList();
		}
		return line;
	}

    public List<WordToken> getSentenceAtIndex(Integer index) {
        List<WordToken> sentenceResult = new ArrayList<>();
        Sentence sentenceAtIndex = null;
        if ((index>0) && (index<sentenceList.size())) {
            sentenceAtIndex = sentenceList.get(index);
            sentenceResult = sentenceAtIndex.getSentenceList();
        }
        return sentenceResult;
    }

    public List<WordToken> getText() {
        List<WordToken> text=new ArrayList<>();
        List<WordToken> sentenceAtIndex=null;
        Sentence sentence=null;
        for (int i=0;i<sentenceList.size();i++) {
            sentence = sentenceList.get(i);
            sentenceAtIndex = sentence.getSentenceList();
            for (WordToken wordToken:sentenceAtIndex) {
               text.add(wordToken);
            }   
          }
        return text;
    }

	public void addSentence(List<WordToken> line) {
		Sentence tokenList = new Sentence();
		tokenList.setSentenceList(line);
		this.sentenceList.add(tokenList);
	}

    public Integer getSentenceCount() {
        Integer count = sentenceList.size();
        return count;
    } 

	public String toJson() {
      String jsonString="[", jsonLine="", strType="";
      Integer count=0;
      List<WordToken> line=null;
      strType = this.getType();
      for (Sentence sentence:sentenceList) {
		   line = sentence.getSentenceList();
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
      if ((strType!=null) && (strType.length()>0)) {
         jsonString = "\"type:\"+\""+strType+"\"" +jsonString;
      }
      jsonString = "{" + jsonString + "}";
      return jsonString;
    }

}