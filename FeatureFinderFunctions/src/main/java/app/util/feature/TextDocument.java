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
    
	public List<WordToken> getSentence(Integer index) {
		List<WordToken> line=null;
		Sentence sentence=null;
		if ((index>0) && (index<sentencelist.size())) {
			sentence = sentencelist.get(index);
			line = sentence.getSentenceList();
		}
		return line;
	}

    public List<WordToken> getSentenceAtIndex(Integer index) {
        List<WordToken> sentenceResult = new ArrayList<>();
        Sentence sentenceAtIndex = null;
        if ((index>0) && (index<sentencelist.size())) {
            sentenceAtIndex = sentencelist.get(index);
            sentenceResult = sentenceAtIndex.getSentenceList();
        }
        return sentenceResult;
    }

    public List<WordToken> getText() {
        List<WordToken> text=new ArrayList<>();
        List<WordToken> sentenceAtIndex=null;
        Sentence sentence=null;
        for (int i=0;i<sentencelist.size();i++) {
            sentence = sentencelist.get(i);
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
		this.sentencelist.add(tokenList);
	}

    public Integer getSentenceCount() {
        Integer count = sentencelist.size();
        return count;
    } 

}