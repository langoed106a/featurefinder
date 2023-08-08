package app.util.feature;

import java.util.ArrayList;
import java.util.List;

import app.util.feature.Section;
import app.util.feature.WordToken;
import edu.washington.cs.knowitall.regex.Match;
import edu.washington.cs.knowitall.regex.RegularExpression;

public class TextRegularExpression  {
        private RegularExpression<WordToken> logicExpression;
        private String regex;
    
    public TextRegularExpression(String string) {
        this.regex = string;
    }

    public void setLogicExpression(RegularExpression logicExpression) {
        this.logicExpression = logicExpression;
    }

    public List<Match<WordToken>> findAll(List<WordToken> wordTokenList) {
        List<Match<WordToken>> matches = logicExpression.findAll(wordTokenList);
        return matches;
    }

    public List<WordToken> match(List<WordToken> wordTokenList, Section section) {
        List<WordToken> wordList = new ArrayList<WordToken>();
        return wordList;
    }

}