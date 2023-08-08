package app.util.feature;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TestSentenceSplitter {
     SentenceSplitter sentenceSplitter;

     public TestSentenceSplitter() {

     }
     @Before
     public void setup() {
         sentenceSplitter = new SentenceSplitter();
     }

     @Test
    public void testGetSentencesSimple1() {
         String sentence="Try this. Try that";
         List<String> lines = sentenceSplitter.getSentences(sentence);
         Assert.assertTrue(lines.size()==2);
     }

    @Test
    public void testGetSentencesSimple2() {
        String sentence="Try this! Try that? Try again.";
        List<String> lines = sentenceSplitter.getSentences(sentence);
        Assert.assertTrue(lines.size()==3);
    }
}
