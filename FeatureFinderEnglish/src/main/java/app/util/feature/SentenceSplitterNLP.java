package app.util.feature;

import java.io.FileInputStream; 
import java.io.InputStream; 
import java.io.InputStreamReader; 
  
import opennlp.tools.sentdetect.SentenceDetectorME; 
import opennlp.tools.sentdetect.SentenceModel; 
import opennlp.tools.util.Span;

import org.springframework.web.context.WebApplicationContext;

public class SentenceSplitterNLP { 
    private SentenceModel model;
    private SentenceDetectorME detector;
  
    public SentenceSplitterNLP(WebApplicationContext applicationContext) { 
        InputStream inputStream;
        try {
           inputStream = applicationContext.getResource("classpath:en-sent.bin").getInputStream();
           model = new SentenceModel(inputStream); 
           detector = new SentenceDetectorME(model); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String[] split(String text) {
       String[] sentenceArray = detector.sentDetect(text); 
       return sentenceArray;
   } 
}