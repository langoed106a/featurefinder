package app.util.feature;

import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;

public class SpellChecker {

    public SpellChecker() {

    }

    public String checkSpelling(String text) {
       String reply="";
       JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en-GB"));
    // comment in to use statistical ngram data:
    //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
       try {
       List<RuleMatch> matches = langTool.check(text);
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