package app.util.feature;
  
import app.util.feature.Section;

import java.util.List;
import java.util.ArrayList;

import org.easymock.EasyMockRunner;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

   @RunWith(EasyMockRunner.class)
   public class TestSection {
       Section section;

       @Before
       public void setUp() throws Exception {
          section = new Section();
        }

        @Test
        public void toJsonValidTest() {
           List<WordToken> wordList = new ArrayList<>();
           String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"wordtype\":\"\",\"dependency\":\"unknown\",\"spacingleft\":\"\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"wordtype\":\"\",\"dependency\":\"unknown\",\"spacingleft\":\"\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"wordtype\":\"\",\"dependency\":\"unknown\",\"spacingleft\":\"\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
           WordToken word1 = new WordToken("try", "try", "MD", "unknown", 0, 0);
           WordToken word2 = new WordToken("this", "this", "MD", "unknown", 1, 0);
           WordToken word3 = new WordToken("out", "out", "MD", "unknown", 2, 0); 
           wordList.add(word1);
           wordList.add(word2);
           wordList.add(word3);
           section.addSentence(wordList);
           String jsonStr = section.toJson();
           assertEquals(jsonStr,outputStr);
        }

        @Test
        public void toJsonEmptyTest() {
           List<WordToken> wordList = new ArrayList<>();
           String outputStr="[{\"linenumber\":\"0\",\"line\":[]}]";
           section.addSentence(wordList);
           String jsonStr = section.toJson();
           assertEquals(jsonStr,outputStr);
        }

}  