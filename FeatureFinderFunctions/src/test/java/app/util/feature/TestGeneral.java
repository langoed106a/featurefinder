package app.util.feature;
  
import app.util.feature.Section;

import java.util.List;
import java.util.ArrayList;

import org.easymock.EasyMockRunner;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

   @RunWith(EasyMockRunner.class)
   public class TestGeneral {
       General general;

       @Before
       public void setUp() throws Exception {
        }

        @Test
        public void getListParametersWithQuotesTest() {
           List<String> paramList=null;
           String strList = "[\"one\",\"two\",\"three\"]";
           paramList = General.getListParameters(strList);
           for (String str:paramList) {
               System.out.println("Value:"+str);
           }
           assertEquals(paramList.size(),3);
        }

        @Test
        public void getListParametersWithoutQuotesTest() {
           List<String> paramList=null;
           String strList = "[one,two,three]";
           paramList = General.getListParameters(strList);
           assertEquals(paramList.size(),3);
        }

        @Test
        public void theSameOneWordTest() {
            String part="postag";
            String wordToCheck="MD";
            List<WordToken> tokenList=new ArrayList<>();
            WordToken word1 = new WordToken("try", "try", "PRP", "unknown", 0, 0);
            WordToken word2 = new WordToken("this", "this", "MD", "unknown", 1, 0);
            WordToken word3 = new WordToken("out", "out", "RB", "unknown", 2, 0); 
            tokenList.add(word1);
            tokenList.add(word2);
            tokenList.add(word3);
            WordToken word = new WordToken("try", "try", "MD", "unknown", 1, 0);
            Boolean theSame=false;
            theSame = General.theSame(part, word, tokenList, wordToCheck);
            assertTrue(theSame);
        }

        @Test
        public void theSameTwoWordTest() {
            String part="token";
            String wordToCheck="try this ";
            List<WordToken> tokenList=new ArrayList<>();
            WordToken word1 = new WordToken("try", "try", "PRP", "unknown", 0, 0);
            WordToken word2 = new WordToken("this", "this", "MD", "unknown", 1, 0);
            WordToken word3 = new WordToken("out", "out", "RB", "unknown", 2, 0); 
            tokenList.add(word1);
            tokenList.add(word2);
            tokenList.add(word3);
            WordToken word = new WordToken("try", "try", "MD", "unknown", 0, 0);
            Boolean theSame=false;
            theSame = General.theSame(part, word, tokenList, wordToCheck);
            assertTrue(theSame);
        }

} 