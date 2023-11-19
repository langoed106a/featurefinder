package app.util.feature;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FileReader implements Runnable  {
    static char[] END_OF_SENTENCE={'.','?','!',':'};
    File filePath;
    String runname;
    Tracker tracker;
    HTTPAsyncSender asyncSender;
            
    public FileReader(File filePath, HTTPAsyncSender asyncSender, Tracker tracker, String runname) {
        this.filePath = filePath;
        this.tracker = tracker;
        this.asyncSender = asyncSender;
        this.runname = runname;
    }

    @SuppressWarnings("unchecked")
    public void run() {
        Boolean finish=false;
        int value;
        char token, eol;
        Map<String, String> params;
        Reader reader=null;
        String line="", reply="";
        StringBuffer strBuffer = null;
        Integer count=0, compare=0, index=0;
        try{      
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.filePath), "UTF-8"));
            strBuffer = new StringBuffer();
            params = new HashMap<>();
            params.put("runname", runname);
            params.put("name", this.filePath.getName());
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
                    reply = asyncSender.sendpost("asyncprocesstext", line, params);
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