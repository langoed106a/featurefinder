package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import app.util.feature.WordToken;
import app.util.feature.TextDocument;

public class TextDocument extends Document {
    private String language;
	List<Sentence> sentencelist;
	
    public TextDocument() {
		super();
        language="";
		sentencelist = new ArrayList<>();
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
        this.sentencelist = sentenceList;
    }

    public List<Sentence> getSentenceList() {
        return this.sentencelist;
    }

    public List<WordToken> getSentenceAtIndex(Integer sentenceIndex) {
        List<WordToken> sentence = new ArrayList<>();
        if ((sentenceIndex>0) && (sentenceIndex<sentencelist.size())) {
            sentence = sentencelist.get(sentenceIndex).getSentenceList();
        }
        return sentence;
    }

	public void addSentence(List<WordToken> line) {
		Sentence tokenList = new Sentence();
		tokenList.setSentenceList(line);
		this.sentencelist.add(tokenList);
	}


}