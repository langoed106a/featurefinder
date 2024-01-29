package app.util.feature;

public class Emoji {
    private String id;
    private String character;
    private String codepoint;
    private String description;
    private String postag;


    public Emoji(String id, String character, String codepoint, String description, String postag) {
        this.id = id;
        this.character = character;
        this.codepoint = codepoint;
        this.description = description;
        this.postag = postag;
    }
}