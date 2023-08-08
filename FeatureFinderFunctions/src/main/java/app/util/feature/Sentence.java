package app.util.feature;

import java.util.List;
import java.util.ArrayList;

public class Sentence {
    public List<String> sentenceList;
    private Integer sentenceLength;
    private String endOfSentence;

    public Sentence() {
        sentenceList = new ArrayList<>();
        sentenceLength = 0;
    }

    public void addPart(String part) {
        sentenceList.add(part);
    }

    public Integer getSentenceLength() {
        return sentenceList.size();
    }

    public void setSentenceLength(Integer sentenceLength) {
        this.sentenceLength = sentenceLength;
    }

    public List<String> getSentenceList() {
        return sentenceList;
    }

    public String getEndOfSentence() {
        return endOfSentence;
    }

    public void setEndOfSentence(String endOfSentence) {
        this.endOfSentence=endOfSentence;
    }

    public String toString() {
        return String.join(" ", sentenceList);
    }

}