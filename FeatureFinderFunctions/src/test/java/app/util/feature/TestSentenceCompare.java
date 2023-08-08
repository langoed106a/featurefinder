package app.util.feature;
  
import app.util.feature.Section;
import app.util.feature.WordStorage;

import java.util.List;
import java.util.ArrayList;

import org.easymock.EasyMockRunner;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.expect;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.easymock.PowerMock;

   @RunWith(PowerMockRunner.class)
   public class TestSentenceCompare {
       SentenceCompare sentenceCompare;

       @Before
       public void setUp() throws Exception {
           sentenceCompare = new SentenceCompare();
        }

        @Test
        public void theSameTest() {
           Boolean found=true;
           List<String> paramList=new ArrayList<>();
           List<WordToken> wordList1 = new ArrayList<>();
           List<WordToken> wordList2 = new ArrayList<>();
           List<PartMatch> sameList = new ArrayList<>();
           SentenceMatch sentenceMatch=null;
           String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
           String part="token";
           String param="un";
           paramList.add(param);
           WordToken word1 = new WordToken("When", "I", "PRP", "unknown", 0, 0);
           WordToken word2 = new WordToken("I", "went", "VB", "unknown", 0, 0);
           WordToken word3 = new WordToken("went", "shopping", "RB", "unknown", 0, 0);
           WordToken word4 = new WordToken("to", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word5 = new WordToken("the", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word6 = new WordToken("club", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word7 = new WordToken("I", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word8 = new WordToken("went", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word9 = new WordToken("for", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word10 = new WordToken("a", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word11 = new WordToken("long", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word12 = new WordToken("walk", "somewhere", "VBP", "unknown", 0, 0);

           WordToken word13 = new WordToken("He", "unwent", "PRP", "unknown", 0, 0);
           WordToken word14 = new WordToken("went", "did", "NN", "unknown", 0, 0);
           WordToken word15 = new WordToken("for", "go", "VB", "unknown", 0, 0);
           WordToken word16 = new WordToken("a", "shopping", "RB", "unknown", 0, 0);
           WordToken word17 = new WordToken("long", "in", "NN", "unknown", 0, 0);
           WordToken word18 = new WordToken("walk", "somewhere", "NN", "unknown", 0, 0);
           WordToken word19 = new WordToken("when", "unwent", "PRP", "unknown", 0, 0);
           WordToken word20 = new WordToken("he", "did", "NN", "unknown", 0, 0);
           WordToken word21 = new WordToken("went", "go", "VB", "unknown", 0, 0);
           WordToken word22 = new WordToken("to", "shopping", "RB", "unknown", 0, 0);
           WordToken word23 = new WordToken("the", "in", "NN", "unknown", 0, 0);
           WordToken word24 = new WordToken("club", "somewhere", "NN", "unknown", 0, 0);
           wordList1.add(word1);
           wordList1.add(word2);
           wordList1.add(word3);
           wordList1.add(word4);
           wordList1.add(word5);
           wordList1.add(word6);
           wordList1.add(word7);
           wordList1.add(word8);
           wordList1.add(word9);
           wordList1.add(word10);
           wordList1.add(word11);
           wordList1.add(word12);
           wordList2.add(word13);
           wordList2.add(word14);
           wordList2.add(word15);
           wordList2.add(word16);
           wordList2.add(word17);
           wordList2.add(word18);
           wordList2.add(word19);
           wordList2.add(word20);
           wordList2.add(word21);
           wordList2.add(word22);
           wordList2.add(word23);
           wordList2.add(word24);
           sentenceMatch = sentenceCompare.theSame(wordList1, "1", wordList2, "2", 2, "", "", 3, false);
           Integer numberMatches = (sentenceMatch.getMatches()).size();
           assertTrue(numberMatches==10);
        }

        @Test
        public void theSameValidTest() {
           Boolean found=true;
           List<String> paramList=new ArrayList<>();
           List<WordToken> wordList1 = new ArrayList<>();
           List<WordToken> wordList2 = new ArrayList<>();
           List<PartMatch> sameList = new ArrayList<>();
           String outputStr= "[{\"linenumber\":\"0\",\"line\":[{\"token\":\"try\",\"lemma\":\"try\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"0\",\"sentence\":\"0\"},{\"token\":\"this\",\"lemma\":\"this\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"1\",\"sentence\":\"0\"},{\"token\":\"out\",\"lemma\":\"out\",\"postag\":\"MD\",\"dependency\":\"unknown\",\"index\":\"2\",\"sentence\":\"0\"}]}]";
           String part="token";
           String param="un";
           SentenceMatch sentenceMatch=null;
           paramList.add(param);
           WordToken word1 = new WordToken("I", "I", "PRP", "unknown", 0, 0);
           WordToken word2 = new WordToken("went", "went", "VB", "unknown", 0, 0);
           WordToken word3 = new WordToken("for", "shopping", "RB", "unknown", 0, 0);
           WordToken word4 = new WordToken("shopping", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word5 = new WordToken("and", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word6 = new WordToken("I", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word7 = new WordToken("smoked", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word8 = new WordToken("and", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word9 = new WordToken("I", "somewhere", "VBP", "unknown", 0, 0);
           WordToken word10 = new WordToken("walked", "somewhere", "VBP", "unknown", 0, 0);

           WordToken word13 = new WordToken("I", "unwent", "PRP", "unknown", 0, 0);
           WordToken word14 = new WordToken("walked", "did", "NN", "unknown", 0, 0);
           WordToken word15 = new WordToken("and", "go", "VB", "unknown", 0, 0);
           WordToken word16 = new WordToken("I", "shopping", "RB", "unknown", 0, 0);
           WordToken word17 = new WordToken("smoked", "in", "NN", "unknown", 0, 0);
           WordToken word18 = new WordToken("and", "somewhere", "NN", "unknown", 0, 0);
           WordToken word19 = new WordToken("I", "unwent", "PRP", "unknown", 0, 0);
           WordToken word20 = new WordToken("went", "did", "NN", "unknown", 0, 0);
           WordToken word21 = new WordToken("shopping", "go", "VB", "unknown", 0, 0);
           wordList1.add(word1);
           wordList1.add(word2);
           wordList1.add(word3);
           wordList1.add(word4);
           wordList1.add(word5);
           wordList1.add(word6);
           wordList1.add(word7);
           wordList1.add(word8);
           wordList1.add(word9);
           wordList1.add(word10);
           wordList2.add(word13);
           wordList2.add(word14);
           wordList2.add(word15);
           wordList2.add(word16);
           wordList2.add(word17);
           wordList2.add(word18);
           wordList2.add(word19);
           wordList2.add(word20);
           wordList2.add(word21);
   
           sentenceMatch = sentenceCompare.theSame(wordList1, "1", wordList2, "2", 2, "", "", 3, true);
           Integer numberMatches = (sentenceMatch.getMatches()).size();
           assertTrue(numberMatches==2);
        }
   }