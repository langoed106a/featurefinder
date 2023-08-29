package app.util.feature;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import app.util.feature.WordToken;

public class TextBlock {

    public TextBlock() {
    }

    @JsonProperty("sentencelist")
    private List<WordToken> sentenceList;

    public String toString() {
        String str="[";
        for (WordToken wordToken:sentenceList) {
            str = str + wordToken.toString();
        }
        str = str + "]";
        return str;
    }

}