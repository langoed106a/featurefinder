package app.util.feature;

import java.util.List;

import app.util.feature.WordToken;
import app.util.feature.Section;

public class TextDocument extends Document {
    private String language;
	List<Sentence> sentenceList;
	
    public TextDocument() {
		super();
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

}