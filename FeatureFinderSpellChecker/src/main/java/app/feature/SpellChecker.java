package app.util.feature;

import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;

public class SpellChecker {
    private JLanguageTool languageTool;

    public SpellChecker() {
        languageTool = new JLanguageTool(Languages.getLanguageForShortCode("en-GB"));
    }

    public String checkSpelling(String text) {
       String reply="";
    // comment in to use statistical ngram data:
    //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
       try {
       List<RuleMatch> matches = languageTool.check(text);
       for (RuleMatch match : matches) {
          System.out.println("Potential error at characters " +
          match.getFromPos() + "-" + match.getToPos() + ": " +
          match.getMessage());
          System.out.println("Suggested correction(s): " +
          match.getSuggestedReplacements());
       }
       } catch (Exception exception) {
           exception.printStackTrace();
       }
     return reply;
    }
}