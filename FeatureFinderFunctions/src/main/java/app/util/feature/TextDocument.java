package app.util.feature;

import java.util.List;

import app.util.feature.WordToken;
import app.util.feature.Section;

public class TextDocument extends Document {
    private String language;
	List<WordToken> tokenList;
	
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
    
	public List<WordToken> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<WordToken> tokenList) {
		this.tokenList = tokenList;
	}

}