package app.util.feature;

import java.util.List;
import java.util.ArrayList;

public class Sentence {
    public List<WordToken> tokenList;

    public Sentence() {
        tokenList = new ArrayList<>();
    }

    public Integer getSentenceLength() {
        return tokenList.size();
    }

    public List<WordToken> getSentenceList() {
        return tokenList;
    }

    public void setSentenceList(List<WordToken> line) {
        tokenList = line;
    }

    public String toString() {
        String line="";
        for (WordToken wordToken:tokenList) {
            line.join(" ", wordToken.getToken());
        }
        return line;
    }

}