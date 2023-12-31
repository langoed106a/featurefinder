package app.util.feature;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import app.util.feature.Section;
import app.util.feature.Sentence;
import app.util.feature.WordToken;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.OpenIE;
import edu.stanford.nlp.naturalli.SentenceFragment;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ie.util.RelationTriple;

@Component
public class ChineseParser {
    private StanfordCoreNLP stanfordParser;
    private Boolean termsLoaded;
   
    public ChineseParser() {
        Properties props = new Properties();
        try {
               props.load(ChineseParser.class.getClassLoader().getResourceAsStream("StanfordCoreNLP-chinese.properties"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }  
        stanfordParser = new StanfordCoreNLP(props);
    }

    @PostConstruct
    private void init() {
        Properties props = new Properties();
        InputStream stream = null;
        try {
               props.load(ChineseParser.class.getClassLoader().getResourceAsStream("StanfordCoreNLP-chinese.properties"));
        } catch (Exception exception) {
             exception.printStackTrace();
        }
//  props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
//  props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,depparse,natlog,openie");
//  props.setProperty("tokenize.options", "ptb3Escaping=false");
   //  props.setProperty("parse.maxlen", "10000");
   //  props.setProperty("depparse.extradependencies", "SUBJ_ONLY");
//  props.setProperty("coref.algorithm", "neural");
       stanfordParser = new StanfordCoreNLP(props);
     }

    public Section parseTextToText(String text) {
        Annotation document=null;
        Integer wordIndex=0;
        Integer sentenceIndex=0;
        List<CoreLabel> coreLabelList=null;
        Section section = new Section();
        String[] wordParts = null, objectParts = null;
        String lemma="", tag="", token="", dependency="", endofline="", graphOfText="", storedWord="", subject="", object="", action="";
        List<WordToken> wordTokenList = null;
        List<WordToken> tokenList = null;
        List<String> wordList=null;
        List<String> sentences = this.getSentences(text);
        WordToken wordToken=null, itemToken=null;
        for (String line:sentences) {
             document = new Annotation(line);
             stanfordParser.annotate(document);
             wordTokenList = new ArrayList<>();
             for (CoreLabel item: document.get(TokensAnnotation.class)) {
                  tag = item.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                  token = item.get(CoreAnnotations.TextAnnotation.class);
                  lemma = item.get(CoreAnnotations.LemmaAnnotation.class);
                  wordToken = new WordToken(token, lemma, tag, " ", wordIndex, sentenceIndex);
                  wordIndex++;
                  wordTokenList.add(wordToken);
             }
             section.addSentence(wordTokenList);
             sentenceIndex++;
             wordIndex=0;
       }
     return section;
     }

     public List<String> getSentences(String text) {
         List<String> sentences=new ArrayList<>();
         String[] delimiters={"。","?","？","!","！","！"};
         String line=text, lineend="", partline="";
         Boolean finish=false, punctuation=false;
         Integer position=text.length(),index=0,delimitersize=0;
         while (!finish) {
            punctuation=false;
            index=0;
            for (int i=0;i<delimiters.length;i++) {
                lineend=delimiters[i];
                delimitersize=lineend.length(); 
                index = line.indexOf(lineend);
                if (index>0) {
                    if (index<position) {
                        position=index;
                        punctuation=true;
                     }
                }
            }
            if (punctuation) {
                  partline=line.substring(0,position+delimitersize);
                  line=line.substring(position+delimitersize, line.length());
                  sentences.add(partline);
                  position=line.length();
            } else {
                 finish=true;
            }
       }
       if (line.length()>0) {
           sentences.add(line);
       }
      return sentences;
    }

    public String wordExists(String listname, String word) {
       return "true";
    }
        
    private List<WordToken> getWordSpacing(List<WordToken> tokens, String text) {
        List<WordToken> lineTokenList=new ArrayList<>();
        Integer wordIndex=0, sentenceIndex=0, lineIndex=0, position=0;
        WordToken lineToken=null;
        String mainText=text, word="", gap="";
        if (tokens.size()>0) {
             for (WordToken wordToken:tokens) {
                 word=wordToken.getToken();
                 if (mainText.startsWith(word)) {
                     mainText = mainText.substring(word.length(), mainText.length());
                     lineToken = new WordToken(word, wordToken.getLemma(), wordToken.getPostag(), wordToken.getDependency(), wordToken.getIndex(), wordToken.getSentence());
                     lineTokenList.add(lineToken);
                 } else {
                     position=mainText.indexOf(word);
                     if (position>0) {
                         gap=mainText.substring(0,position);
                         mainText=mainText.substring(position+word.length(), mainText.length());
                         lineToken=new WordToken(word, wordToken.getLemma(), wordToken.getPostag(), wordToken.getDependency(), wordToken.getIndex(), wordToken.getSentence());
                         lineToken.setSpacingLeft(gap);
                         lineTokenList.add(lineToken);
                     }
                }
            }
       }
       return lineTokenList;
    }

}