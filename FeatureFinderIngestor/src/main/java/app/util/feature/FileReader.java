package app.util.feature;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

public class FileReader implements Runnable  {
    static char[] END_OF_SENTENCE={'.','?','!',':'};
    File filePath;
    String tokenid;
    Tracker tracker;
    RemoteProcessor remoteProcessor;
            
    public FileReader(File filePath, RemoteProcessor remoteProcessor, Tracker tracker, String tokenid) {
        this.filePath = filePath;
        this.tracker = tracker;
        this.remoteProcessor = remoteProcessor;
        this.tokenid = tokenid;
    }

    @SuppressWarnings("unchecked")
    public void run() {
        Boolean finish=false;
        int value;
        char token, eol;
        Reader reader=null;
        String line="", reply="";
        StringBuffer strBuffer = null;
        Integer count=0, compare=0, index=0;
        try{      
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.filePath), "UTF-8"));
            strBuffer = new StringBuffer();
            while ((value = reader.read()) != -1) {
                finish = false;
                index=0;
                token=(char)value;
                while ((!finish) && (index<END_OF_SENTENCE.length)) {
                   eol = END_OF_SENTENCE[index];
                   if (token==eol) {
                      finish = true;
                   } 
                   index++;
                }
                strBuffer.append(token);
                if (finish) {
                    line = strBuffer.toString();
                    reply = remoteProcessor.processText(line, tokenid);
                    strBuffer = new StringBuffer();
                }
            }
            reader.close();
            tracker.jobCompleted();
        } catch (Exception exception) {
            exception.printStackTrace();
            tracker.jobCompleted();
        }
    }
}