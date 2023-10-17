package app.util.feature;

import java.util.List;

public class ResultDocument extends TextDocument {
    List<WordToken> matches;

    public List<WordToken> getMatches() {
        return this.matches;
    }

    public void setMatches(List<WordToken> matches) {
        this.matches = matches;
    }
}