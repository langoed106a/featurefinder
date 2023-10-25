package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import app.util.feature.WordToken;
import app.util.feature.TextDocument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TextDocument extends Document {
    private String language;

    @JsonProperty("sentencelist")
    private List<Sentence> sentencelist;
	
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
        this.sentencelist = new ArrayList<>();
    }
    
    public String getLanguage() {
    	return language;
    }

    public void setLanguage(String language) {
    	this.language = language;
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

    @JsonIgnore
    public List<Sentence> getSentenceList() {
        return this.sentencelist;
    }

    @JsonIgnore
    public List<WordToken> getSentenceAtIndex(Integer sentenceIndex) {
        List<WordToken> sentence = new ArrayList<>();
        if ((sentenceIndex>0) && (sentenceIndex<=sentencelist.size())) {
            sentence = sentencelist.get(sentenceIndex-1).getTokenList();
        }
        return sentence;
    }

    @JsonIgnore
    public List<WordToken> getAsOneSentence() {
        Integer index=0;
        List<WordToken> line = null;
        List<WordToken> allLines = new ArrayList<>();
        WordToken tempToken=null;
        for (Sentence sentence:sentencelist) {
             line = sentence.getTokenList();
             for (WordToken wordToken:line) {
                tempToken = wordToken.clone();
                tempToken.setSentence(1);
                tempToken.setIndex(index);
                allLines.add(tempToken);
                index++;
             }
        }
        return allLines;
    }

    @JsonIgnore
    public Integer getSentenceCount() {
        Integer count = this.sentencelist.size();
        return count;
    }

	public void addSentence(Sentence line) {
		this.sentencelist.add(line);
	}

    public void fromJson(String jsonStr) {
         TextDocument textDocument = null;
         try {
              textDocument = new ObjectMapper().readValue(jsonStr, TextDocument.class);
              this.setContents(textDocument.getContents());
              this.setId(textDocument.getId());
              this.setLanguage(textDocument.getLanguage());
              this.setType(textDocument.getType());
              this.setName(textDocument.getName());
              this.setSentenceList(textDocument.getSentenceList());
         } catch (Exception exception) {
             exception.printStackTrace();
         }
    }

    public String toJson() {
        String jsonStr = "";
        try {
            jsonStr = new ObjectMapper().writeValueAsString(this);
        } catch (Exception exception) {
                exception.printStackTrace();
        }
        return jsonStr;
    }

}