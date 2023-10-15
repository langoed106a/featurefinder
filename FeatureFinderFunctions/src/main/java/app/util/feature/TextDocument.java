package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import app.util.feature.WordToken;
import app.util.feature.Section;

public class TextDocument extends Document {
    private String language;
	List<Sentence> sentenceList;
	
    public TextDocument() {
		super();
		sentenceList = new ArrayList<>();
	}

    public TextDocument(String name, String type, String language, String contents) {
    	this.name = name;
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
    
	public List<WordToken> getSentence(Integer index) {
		List<WordToken> line=null;
		Sentence sentence=null;
		if ((index>0) && (index<sentenceList.size())) {
			sentence = sentenceList.get(index);
			line = sentence.getSentenceList();
		}
		return line;
	}

	public void addSentence(List<WordToken> line) {
		Sentence tokenList = new Sentence();
		tokenList.setSentenceList(line);
		this.sentenceList.add(tokenList);
	}

	public String toJson() {
      String jsonString="[", jsonLine="";
      Integer count=0;
      for (Sentence line:sentenceList) {
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
      return jsonString;
    }


	}

}