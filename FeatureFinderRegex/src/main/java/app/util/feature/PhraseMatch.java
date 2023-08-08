package app.util.feature;

public class PhraseMatch {
    private Integer startWordIndex;
    private Integer endWordIndex;
    private String id;
    private String linkedId;
    private String phrase;
    
    public PhraseMatch(Integer startIndex, Integer endIndex, String phrase) {
        this.startWordIndex = startIndex;
        this.endWordIndex = endIndex;
        this.phrase = phrase;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }
    public String getLinkedId() {
        return linkedId;
    }
    public Integer getStartWordIndex() {
        return startWordIndex;
    }
    
    public void setStartWordIndex(Integer startWordIndex) {
        this.startWordIndex = startWordIndex;
    }
    
    public Integer getEndWordIndex() {
        return endWordIndex;
    }
    
    public void setEndWordIndex(Integer endWordIndex) {
        this.endWordIndex = endWordIndex;
    }
    
    public String getPhrase() {
        return phrase;
    }
    
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}