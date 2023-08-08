package app.util.feature;
  
import app.util.feature.SentenceSplitter;

import java.util.List;

import org.easymock.EasyMockRunner;


import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

   @RunWith(EasyMockRunner.class)
   public class TestSentenceSplitter {
       SentenceSplitter sentenceSplitter;

       @Before
       public void setUp() throws Exception {
          sentenceSplitter = new SentenceSplitter();
        }

        @Test
        public void getEmptySentencesTest() {
           String text="";
           List<String> lines = sentenceSplitter.getSentences(text);
           assertEquals(lines.size(),0);
        }

        @Test
        public void getSingleSentencesTest() {
           String text="try this out";
           List<String> lines = sentenceSplitter.getSentences(text);
           assertEquals(lines.size(),1);
        }

        @Test
        public void getDoubleSentencesTest() {
           String text="Try this out.Try this out ";
           List<String> lines = sentenceSplitter.getSentences(text);
           assertEquals(lines.size(),2);
        }
}  