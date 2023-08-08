package app.util.feature;

import app.util.feature.FeatureFunction; 
import app.util.feature.RegexFeature; 
import app.util.feature.Section; 
import app.util.feature.WordStorage; 
import app.util.feature.WordToken; 
import app.util.feature.ContractFunction; 
import app.util.feature.Matcher; 
import app.util.feature.ParseRegexException; 

import org.junit.Before; 
import org.junit.Test; 
import org.junit.runner.RunWith; 

import static org.junit.Assert.assertTrue; 
import static org.junit.Assert.assertFalse; 

import java.util.ArrayList; 
import java.util.List; 
import java.util.Properties;

import org.easymock.EasyMockRunner; 
import org.easymock.Mock; 

@RunWith(EasyMockRunner.class) 
public class TestContractFunction { 
       private ContractFunction contractFunction; 

       @Mock 
       FeatureFunction featureFunction; 
       @Mock 
       WordStorage wordStorage; 

       public TestContractFunction() { 

       } 

       @Before 
       public void setUp() { 
              contractFunction = new ContractFunction(featureFunction, wordStorage); 
       } 

       @Test 
       public void testDoValidPreCondition() { 
              RegexFeature regexFeature = new RegexFeature("test", "test", "test", "<token=the>", "text", "<firsttoken=the>", ""); 
              Section section = new Section(); 
              List<WordToken> wordList = new ArrayList<>(); 
              WordToken word1 = new WordToken("the","the","DT","unknown",0,0); 
              WordToken word2 = new WordToken("end","end","NN","unknown",1,0); 
              wordList.add(word1); 
              wordList.add(word2); 
              section.addSentence(wordList); 
              Boolean passed = contractFunction.doPrecondition(regexFeature,section); 
              assertTrue(passed); 
       } 

       @Test 
       public void testDoInValidPreCondition() { 
              RegexFeature regexFeature = new RegexFeature("test", "test", "test", "<token=the>", "text", "<token=and>", ""); 
              Section section = new Section(); 
              List<WordToken> wordList = new ArrayList<>(); 
              WordToken word1 = new WordToken("the","the","DT","unknown",0,0); 
              WordToken word2 = new WordToken("end","end","NN","unknown",1,0); 
              wordList.add(word1); 
              wordList.add(word2); 
              section.addSentence(wordList); 
              Boolean passed = contractFunction.doPrecondition(regexFeature,section); 
              assertFalse(passed); 
       } 

       @Test 
       public void testDoValidGreaterPostCondition() { 
              RegexFeature regexFeature = new RegexFeature("test", "test", "test", "<token=the>", "text", "", "$matches>2"); 
              Properties textProperties = new Properties();
              textProperties.setProperty("$matches","3");
              Boolean passed = contractFunction.doPostcondition(regexFeature,textProperties); 
              assertTrue(passed); 
       } 

       @Test 
       public void testDoValidGreaterEqualPostCondition() { 
              RegexFeature regexFeature = new RegexFeature("test", "test", "test", "<token=the>", "text", "", "$matches>=2"); 
              Properties textProperties = new Properties();
              textProperties.setProperty("$matches","2");
              Boolean passed = contractFunction.doPostcondition(regexFeature,textProperties); 
              assertTrue(passed); 
       } 

       @Test 
       public void testDoValidLessPostCondition() { 
              RegexFeature regexFeature = new RegexFeature("test", "test", "test", "<token=the>", "text", "", "$matches<2");
              Properties textProperties = new Properties(); 
              textProperties.setProperty("$matches","1");
              Boolean passed = contractFunction.doPostcondition(regexFeature,textProperties); 
              assertTrue(passed); 
       } 

       @Test 
       public void testInValidExprPostCondition() { 
              RegexFeature regexFeature = new RegexFeature("test", "test", "test", "<token=the>", "text", "", "$matches&=2)");
              Properties textProperties = new Properties(); 
              textProperties.setProperty("$matches","1");
              Boolean passed = contractFunction.doPostcondition(regexFeature,textProperties); 
              assertFalse(passed); 
       } 
 } 