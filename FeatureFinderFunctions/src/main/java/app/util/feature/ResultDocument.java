package app.util.feature;

import java.util.List;

public class ResultDocument extends TextDocument {
    List<String> matches;

    public List<String> getMatches() {
        return this.matches;
    }

    public void setMatches(List<String> matches) {
        this.matches = matches;
    }
}