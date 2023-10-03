package app.util.feature;
  
import app.util.feature.Section;
import app.util.feature.WordStorage;

import java.util.List;
import java.util.ArrayList;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.easymock.PowerMock;

   @RunWith(PowerMockRunner.class)
   public class TestFeatureFunctionList {
       FeatureFunctionList featureFunctionList;

       @Mock
       private WordStorage wordStorageMock;

       @Before
       public void setUp() throws Exception {
           featureFunctionList = new FeatureFunctionList();
        }

        @Test
        public void validwithoutprefixTest() {
           Boolean found=false;
           List<String> paramList=new ArrayList<>();
           List<WordToken> wordList = new ArrayList<>();
           String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
           String part="token";
           String param="un";
           paramList.add(param);
           Section section = new Section();
           WordToken word1 = new WordToken("unwent", "unwent", "MD", "unknown", 0, 0);
           wordList.add(word1);
           section.addSentence(wordList);
           WordStorage wordStorageMock = mock(WordStorage.class);
           expect(wordStorageMock.wordExists("commonword", "went")).andReturn(true);
           expect(wordStorageMock.wordExists("commonword", "unwent")).andReturn(false);
           PowerMock.replay(wordStorageMock);
           featureFunctionList.setWordStorage(wordStorageMock);
           found = featureFunctionList.validwithoutprefix(part, word1, section, paramList);
           assertTrue(found);
        }

        @Test
        public void validwithprefixValidTest() {
           Boolean found=false;
           List<String> paramList=new ArrayList<>();
           List<WordToken> wordList = new ArrayList<>();
           String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
           String part="token";
           String param="pre";
           paramList.add(param);
           Section section = new Section();
           WordToken word1 = new WordToken("defined", "defined", "MD", "unknown", 0, 0);
           wordList.add(word1);
           section.addSentence(wordList);
           WordStorage wordStorageMock = mock(WordStorage.class);
           expect(wordStorageMock.wordExists("commonword", "predefined")).andReturn(true);
           PowerMock.replay(wordStorageMock);
           featureFunctionList.setWordStorage(wordStorageMock);
           found = featureFunctionList.validwithprefix(part, word1, section, paramList);
           assertTrue(found);
        } 

        @Test
        public void validwithprefixInValidTest() {
           Boolean found=false;
           List<String> paramList=new ArrayList<>();
           List<WordToken> wordList = new ArrayList<>();
           String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
           String part="token";
           String param="pre";
           paramList.add(param);
           Section section = new Section();
           WordToken word1 = new WordToken("went", "went", "MD", "unknown", 0, 0);
           wordList.add(word1);
           section.addSentence(wordList);
           WordStorage wordStorageMock = mock(WordStorage.class);
           expect(wordStorageMock.wordExists("commonword", "prewent")).andReturn(false);
           PowerMock.replay(wordStorageMock);
           featureFunctionList.setWordStorage(wordStorageMock);
           found = featureFunctionList.validwithprefix(part, word1, section, paramList);
           assertFalse(found);
        }  

       @Test
       public void lengthmorethanValidTest() {
         Boolean found=false;
         List<String> paramList=new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
         String part="token";
         String param="5";
         Section section = new Section();
         WordToken word1 = new WordToken("unwent", "unwent", "MD", "unknown", 0, 0);
         wordList.add(word1);
         section.addSentence(wordList);
         paramList.add(param);
         found = featureFunctionList.lengthmorethan(part, word1, section, paramList);
         assertTrue(found);
       }

       @Test
       public void lengthmorethanInValidTest() {
         Boolean found=false;
         List<String> paramList=new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
         String part="token";
         String param="one";
         Section section = new Section();
         WordToken word1 = new WordToken("unwent", "unwent", "MD", "unknown", 0, 0);
         wordList.add(word1);
         section.addSentence(wordList);
         paramList.add(param);
         found = featureFunctionList.lengthmorethan(part, word1, section, paramList);
         assertFalse(found);
       }
	
       @Test
       public void lowerWordValid() {
         Boolean found=false;
         List<String> paramList=new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
         String part="token";
         String param="I";
         Section section = new Section();
         WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("TRIED", "tried", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("OUT", "out", "MD", "unknown", 3, 0);
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         section.addSentence(wordList);
         paramList.add(param);
         found = featureFunctionList.lower("token", word3, section, paramList);
         assertTrue(found);
       }

       @Test
       public void lowerWordInListValid() {
         Boolean found=false;
         List<String> paramList=new ArrayList<>();
         List<String> commonWordList=new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
         String part="token";
         String param="$dummycommonword";
         Section section = new Section();
         WordStorage wordStorageMock = mock(WordStorage.class);
         commonWordList.add("of course");
         commonWordList.add("why not");
         FeatureStore featureStoreMock = mock(FeatureStore.class);
         featureFunctionList.setFeatureStore(featureStoreMock);
         featureFunctionList.setWordStorage(wordStorageMock);
         WordToken word1 = new WordToken("of", "of", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("course", "course", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("THIS", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("OUT", "out", "MD", "unknown", 3, 0);
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         section.addSentence(wordList);
         expect(featureStoreMock.getFeatureByName("dummycommonword")).andReturn(null);
         expect(wordStorageMock.wordExists("dummycommonword",word1.getToken())).andReturn(true);
         PowerMock.replay(wordStorageMock);
         PowerMock.replay(featureStoreMock);
         paramList.add(param);
         found = featureFunctionList.lower("token", word1, section, paramList);
         assertTrue(found);
       }

       @Test
       public void existsBeforeValidTest() {
         Boolean found=false;
         List<String> paramList=new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
         String part="token";
         String param="I";
         Section section = new Section();
         WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         section.addSentence(wordList);
         paramList.add(param);
         found = featureFunctionList.existsbefore("token", word3, section, paramList);
         assertTrue(found);
       }

       @Test
       public void existsBeforeInValidTest() {
         Boolean found=false;
         Integer index=0;
         List<String> paramList=new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
         String part="token";
         String param="out";
         Section section = new Section();
         WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         section.addSentence(wordList);
         paramList.add(param);
         index=2;
         found = featureFunctionList.existsbefore("token", word2, section, paramList);
         assertFalse(found);
       }

       @Test
       public void existsAfterValidTest() {
         Boolean found=false;
         List<String> paramList=new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
         String part="token";
         String param="out";
         Section section = new Section();
         WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         section.addSentence(wordList);
         paramList.add(param);
         found = featureFunctionList.existsafter("token", word2, section, paramList);
         assertTrue(found);
       }

     @Test
     public void positionbeforeValidTest() {
        String param1="this";
        String param2="5";
        String part="token";
        List<String> paramList = new ArrayList<>();
        List<WordToken> wordList = new ArrayList<>();
        Section section = new Section();
        WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
        WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
        WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
        WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
        wordList.add(word1);
        wordList.add(word2);
        wordList.add(word3);
        wordList.add(word4);
        section.addSentence(wordList);
        paramList.add(param1);
        paramList.add(param2);
        Boolean found = featureFunctionList.positionbefore(part, word1, section, paramList);
        assertTrue(found);
     }

     @Test
     public void positionbeforeDoubleValidTest() {
        String param1="this out";
        String param2="5";
        String part="token";
        List<String> paramList = new ArrayList<>();
        List<WordToken> wordList = new ArrayList<>();
        Section section = new Section();
        WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
        WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
        WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
        WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
        wordList.add(word1);
        wordList.add(word2);
        wordList.add(word3);
        wordList.add(word4);
        section.addSentence(wordList);
        paramList.add(param1);
        paramList.add(param2);
        Boolean found = featureFunctionList.positionbefore(part, word1, section, paramList);
        assertTrue(found);
     }

     @Test
     public void positionbeforeInValidTest() {
        String param1="this";
        String param2="1";
        String part="token";
        List<String> paramList = new ArrayList<>();
        List<WordToken> wordList = new ArrayList<>();
        Section section = new Section();
        WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
        WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
        WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
        WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
        wordList.add(word1);
        wordList.add(word2);
        wordList.add(word3);
        wordList.add(word4);
        section.addSentence(wordList);
        paramList.add(param1);
        paramList.add(param2);
        Boolean found = featureFunctionList.positionbefore(part, word1, section, paramList);
        assertFalse(found);
     }

     @Test
     public void punctuationValidTest() {
        String param1="this";
        String param2="1";
        String part="token";
        List<String> paramList = new ArrayList<>();
        List<WordToken> wordList = new ArrayList<>();
        Section section = new Section();
        WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
        WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
        WordToken word3 = new WordToken("!", "this", "MD", "unknown", 2, 0);
        WordToken word4 = new WordToken("this", "this", "MD", "unknown", 2, 0);
        WordToken word5 = new WordToken(",", "this", "MD", "unknown", 2, 0);
        WordToken word6 = new WordToken("out", "out", "MD", "unknown", 3, 0);
        wordList.add(word1);
        wordList.add(word2);
        wordList.add(word3);
        wordList.add(word4);
        section.addSentence(wordList);
        Boolean found = featureFunctionList.punctuation(part, word3, section, paramList);
        assertTrue(found);
     }

     @Test
     public void punctuationInValidTest() {
        String param1="this";
        String param2="1";
        String part="token";
        List<String> paramList = new ArrayList<>();
        List<WordToken> wordList = new ArrayList<>();
        Section section = new Section();
        WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
        WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
        WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
        WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
        wordList.add(word1);
        wordList.add(word2);
        wordList.add(word3);
        wordList.add(word4);
        section.addSentence(wordList);
        Boolean found = featureFunctionList.punctuation(part, word1, section, paramList);
        assertFalse(found);
     }
 
      @Test
      public void notmorethanWordValidTest() {
         String param1="$token";
         String param2="5";
         String part="token";
         List<String> paramList = new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         Section section = new Section();
         WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         paramList.add(param1);
         paramList.add(param2);
         section.addSentence(wordList);
         Boolean found = featureFunctionList.notmorethan(part, word1, section, paramList);
         assertTrue(found);
      }

      @Test
      public void spacingleftValidTest() {
         String param1="  ";
         String part="token";
         List<String> paramList = new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         Section section = new Section();
         WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
         word1.setSpacingLeft("  ");
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         paramList.add(param1);
         section.addSentence(wordList);
         Boolean found = featureFunctionList.spacingleft(part, word1, section, paramList);
         assertTrue(found);
      }

      @Test
      public void containsValidTest() {
            String param1="that";
            String param2="there";
            String param3="this";
            String part="token";
            List<String> paramList = new ArrayList<>();
            List<WordToken> wordList = new ArrayList<>();
            Section section = new Section();
            WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
            WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
            WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
            WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
            wordList.add(word1);
            wordList.add(word2);
            wordList.add(word3);
            wordList.add(word4);
            paramList.add(param1);
            paramList.add(param2);
            paramList.add(param3);
            section.addSentence(wordList);
            Boolean found = featureFunctionList.contains(part, word1, section, paramList);
            assertTrue(found);
      }

      @Test
      public void containsInValidTest() {
            String param1="that";
            String part="token";
            List<String> paramList = new ArrayList<>();
            List<WordToken> wordList = new ArrayList<>();
            Section section = new Section();
            WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
            WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
            WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
            WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
            wordList.add(word1);
            wordList.add(word2);
            wordList.add(word3);
            wordList.add(word4);
            paramList.add(param1);
            section.addSentence(wordList);
            Boolean found = featureFunctionList.contains(part, word1, section, paramList);
            assertFalse(found);
      }

      @Test
      public void spacingleftInValidTest() {
         String param1="  ";
         String part="token";
         List<String> paramList = new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         Section section = new Section();
         WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
         word1.setSpacingLeft("");
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         paramList.add(param1);
         section.addSentence(wordList);
         Boolean found = featureFunctionList.spacingleft(part, word1, section, paramList);
         assertFalse(found);
      }

      @Test
      public void notmorethanWordInValidTest() {
         String param1="$token";
         String param2="3";
         String part="token";
         List<String> paramList = new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         Section section = new Section();
         WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         paramList.add(param1);
         paramList.add(param2);
         section.addSentence(wordList);
         Boolean found = featureFunctionList.notmorethan(part, word1, section, paramList);
         assertFalse(found);
      }

      @Test
      public void notmorethanSentenceValidTest() {
         String param1="$sentence";
         String param2="1";
         String part="token";
         List<String> paramList = new ArrayList<>();
         List<WordToken> wordList = new ArrayList<>();
         Section section = new Section();
         WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
         WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
         WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
         WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
         wordList.add(word1);
         wordList.add(word2);
         wordList.add(word3);
         wordList.add(word4);
         paramList.add(param1);
         paramList.add(param2);
         section.addSentence(wordList);
         Boolean found = featureFunctionList.notmorethan(part, word1, section, paramList);
         assertTrue(found);
      }
 

     @Test
     public void symbolValidTest() {
        String param1=",\"";
        String part="token";
        List<String> paramList = new ArrayList<>();
        List<WordToken> wordList = new ArrayList<>();
        Section section = new Section();
        WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
        WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
        WordToken word3 = new WordToken("\"", "this", "MD", "unknown", 2, 0);
        WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
        wordList.add(word1);
        wordList.add(word2);
        wordList.add(word3);
        wordList.add(word4);
        paramList.add(param1);
        section.addSentence(wordList);
        Boolean found = featureFunctionList.symbol(part, word3, section, paramList);
        assertTrue(found);
     }
     @Test
     public void symbolInValidTest() {
        String param1=",";
        String part="token";
        List<String> paramList = new ArrayList<>();
        List<WordToken> wordList = new ArrayList<>();
        Section section = new Section();
        WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
        WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
        WordToken word3 = new WordToken("this", "this", "MD", "unknown", 2, 0);
        WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
        wordList.add(word1);
        wordList.add(word2);
        wordList.add(word3);
        wordList.add(word4);
        paramList.add(param1);
        section.addSentence(wordList);
        Boolean found = featureFunctionList.symbol(part, word3, section, paramList);
        assertFalse(found);
     }

     @Test
     public void misspeltdoublevowelInValidTest() {
        String param1="";
        String part="token";
        List<String> paramList = new ArrayList<>();
        List<WordToken> wordList = new ArrayList<>();
        Section section = new Section();
        WordToken word1 = new WordToken("I", "I", "MD", "unknown", 0, 0);
        WordToken word2 = new WordToken("tried", "tried", "MD", "unknown", 1, 0);
        WordToken word3 = new WordToken("sence", "this", "MD", "unknown", 2, 0);
        WordToken word4 = new WordToken("out", "out", "MD", "unknown", 3, 0);
        wordList.add(word1);
        wordList.add(word2);
        wordList.add(word3);
        wordList.add(word4);
        paramList.add(param1);
        section.addSentence(wordList);
        expect(wordStorageMock.wordExists("commonword","sance")).andReturn(false);
        expect(wordStorageMock.wordExists("commonword","sence")).andReturn(false);
        expect(wordStorageMock.wordExists("commonword","sonce")).andReturn(false);
        expect(wordStorageMock.wordExists("commonword","sunce")).andReturn(false);
        expect(wordStorageMock.wordExists("commonword","since")).andReturn(true);
        expect(wordStorageMock.wordExists("commonword", word3.getToken())).andReturn(false);
        replay(wordStorageMock);
        featureFunctionList.setWordStorage(wordStorageMock);
        Boolean found = featureFunctionList.misspeltdoublevowel(part, word3, section, paramList);
        assertFalse(found);
     }
} 