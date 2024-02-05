package app.util.feature;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sentence {

    @JsonProperty("sentence")
    public List<WordToken> tokenList;

    @JsonProperty("text")
    public String text;

    public Sentence() {
        tokenList = new ArrayList<>();
    }

    @JsonIgnore
    public List<WordToken> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<WordToken> line) {
        tokenList=line;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text=text;
    }

    public String toString() {
        String line="text:"+text+",";
        for (WordToken wordToken:tokenList) {
            line.join(" ", wordToken.getToken());
        }
        return line;
    }

}