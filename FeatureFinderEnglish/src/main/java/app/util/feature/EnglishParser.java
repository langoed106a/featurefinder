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
import org.springframework.web.context.WebApplicationContext;

import app.util.feature.Sentence;
import app.util.feature.TextDocument;
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

public class EnglishParser {
    private StanfordCoreNLP stanfordParser;
    private Boolean termsLoaded;
    private SentenceSplitterNLP sentenceSplitter;
    private WebApplicationContext applicationContext;
    private Map<String, Emoji> emojiMap;
    private EmojiChecker emojiChecker;
    private WordStorage wordStorage;
   
    public EnglishParser(WebApplicationContext applicationContext, WordStorage wordStorage, Map<String, Emoji> emojiMap) {
        Properties props = new Properties();
        this.applicationContext = applicationContext;
        this.wordStorage = wordStorage;
        this.emojiMap = emojiMap;
        this.emojiChecker = new EmojiChecker(emojiMap);
        sentenceSplitter = new SentenceSplitterNLP(applicationContext);

        try {
               props.setProperty("annotators","tokenize,ssplit,pos,lemma,parse");
               props.setProperty("tokenize.options","ptb3Escaping=false");
               props.setProperty("parse.maxlen","10000");
               props.setProperty("coref.algorithm","neural");
               stanfordParser = new StanfordCoreNLP(props);
        } catch (Exception exception) {
            exception.printStackTrace();
        }  
    }

    public TextDocument parseText(String text) {
        Annotation document=null;
        Integer wordIndex=0;
        Integer sentenceIndex=1;
        List<CoreLabel> coreLabelList=null;
        Sentence sentence = null;
        String[] wordParts = null, objectParts = null;
        String str="",lemma="", tag="", token="", dependency="", endofline="", graphOfText="", storedWord="", subject="", object="", action="", newline="";
        TextDocument textDocument = new TextDocument();
        List<WordToken> wordTokenList = null;
        List<WordToken> tokenList = null;
        List<String> wordList=null;
        List<UTFCharacter> characterList=null;
        Character ch=null;
        char[] charArray = null;
        List<String> sentences = this.getSentences(text);
        WordToken wordToken=null, itemToken=null;
        for (String line:sentences) {
            /*
             characterList = emojiChecker.getUTFList(line);
             for (UTFCharacter character: characterList) {
                if (character.getType().equalsIgnoreCase("emoji")) {
                    charArray = Character.toChars(0x1F600);
                    ch = charArray[0];
                } else {
                    ch = character.getCharacter();
                }
                newline = newline + ch;
             }
             */
             document = new Annotation(line);
             stanfordParser.annotate(document);
             wordTokenList = new ArrayList<>();
             for (CoreLabel item: document.get(TokensAnnotation.class)) {
                  tag = item.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                  token = item.originalText();
                  lemma = item.get(CoreAnnotations.LemmaAnnotation.class);
                  wordToken = new WordToken(token, lemma, tag, " ", wordIndex, sentenceIndex);
                  wordIndex++;
                  wordTokenList.add(wordToken);
             }
             sentence = new Sentence();
             sentence.setText(line);
             sentence.setTokenList(wordTokenList);
             textDocument.addSentence(sentence);
             sentenceIndex++;
             wordIndex=0;
       }
     return textDocument;
     }

     public List<String> getSentences(String text) {
        String[] lineArray = sentenceSplitter.split(text);
        List<String> lines = Arrays.asList(lineArray);
        return lines;
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