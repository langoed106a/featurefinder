package app.util.feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import  org.springframework.core.io.Resource;
    
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
    
public class WordStorage {
   private static final Logger logger=LoggerFactory.getLogger(WordStorage.class);
   private static String WORD_LIST_FOLDER="wordlists";
   private static String WORD_FILE_EXTENSION=".dat";
   private BigInteger[] commonHashWords;
   private Map<String, List<String>> wordsMap;
   private WordSearch wordSearch;
   private PostagSearch postagSearch;
       
   public WordStorage(WebApplicationContext applicationContext) {
        this.wordSearch = new WordSearch();
        this.postagSearch = new PostagSearch();
        wordsMap = new HashMap<>();
        this.readWordLists(applicationContext);
    }

    public Map<String, String> getNameofWordLists() {
        Map<String,String> wordListMap = new HashMap<>();
        List<String> nameList = new ArrayList<>(wordsMap.keySet());
        for (String name:nameList) {
            wordListMap.put(name,"words from an internal word list");
        }
      return wordListMap;
    }

    public void readWordLists(WebApplicationContext applicationContext) {
       List<String> wordsList=new ArrayList<>();
       Resource[] resourceList=null;
       Resource resource = null;
       String listname="", filename="", path="";
       URL url = null;
       try {
            resourceList = applicationContext.getResources("classpath:"+WORD_LIST_FOLDER+"/*"+WORD_FILE_EXTENSION);
       } catch (Exception exception) {
            exception.printStackTrace();
       }
       if (resourceList!=null) {
           for (int i=0; i<resourceList.length; i++) {
              resource = resourceList[i];
              try {
                   filename = resource.getFilename();
              } catch (Exception exception) {
                logger.error("Unable to get filename from resource:"+resource.toString());
              }
              wordsList = new ArrayList<>();
              wordsList = this.readResource(resource);
              listname = filename.replace(WORD_FILE_EXTENSION,"");
              logger.info("Reading word list:"+listname);
              wordsMap.put(listname, wordsList);
           }
       }
    }

    public Boolean addWordList(String listname, List<String> list) {
        Boolean added = false;
        listname = listname.toLowerCase();
        if (!wordsMap.containsKey(listname)) {
            wordsMap.put(listname, list);
            added = true;
        }
     return added;
    }
    
    public List<String> getWordList(String listname) {
        List<String> list = new ArrayList<>();
        listname = listname.toLowerCase();
        if (wordsMap.containsKey(listname)) {
            list = wordsMap.get(listname);
        }
     return list;
    }
    
    public boolean wordExists(String listname, WordToken wordToken, List<WordToken> sentence, String part) {
        Boolean exists = false;
        Integer index=0;
        String listItem="";
        List<String> wordList;
        wordList = this.getWordList(listname);
        index=0;
        while ((!exists) && (index<wordList.size())) {
            listItem = wordList.get(index);
            exists = General.theSame(part, wordToken, sentence, listItem);
            index++;
        }    
        return exists;
    }

    public boolean wordExists(String listname, String word) {
        Boolean exists = false;
        Integer index=0;
        String listItem="";
        List<String> wordList;
        wordList = this.getWordList(listname);
        String[] wordArray = new String[wordList.size()];
        wordList.toArray(wordArray); // fill the array
        Integer result = Arrays.binarySearch(wordArray,word); 
        if (result>-1) {
            exists = true;
        }
        return exists;
    }

    public boolean listExists(String listname) {
        Boolean exists = false;
        if (wordsMap.containsKey(listname)) {
            exists = true;
        }
    return exists;
    }
   
    public List<String> readResource(Resource resource) {
       BufferedReader bufferedReader=null;
       InputStream inputStream=null;
       InputStreamReader inputStreamReader = null;
       List<String> wordsList = new ArrayList<>();
       String line="";
       try {
            inputStream = resource.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            line = bufferedReader.readLine();
            while (line!=null) {
                if ((line!=null) && (line.length()>0)) {
                   wordsList.add(line);
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception exception) {
              exception.printStackTrace();
        }
       return wordsList;
    }

    public List<Object> readJsonResource(String filename) {
    BufferedReader bufferedReader=null;
    JSONParser parser = new JSONParser();
    InputStream inputStream=null;
    InputStreamReader inputStreamReader = null;
    List<Object> lines = new ArrayList<>();
    Object jsonObject = null;
    String line="";
    try {
    inputStream = WordStorage.class.getClassLoader().getResourceAsStream(filename);
    inputStreamReader = new InputStreamReader(inputStream);
    bufferedReader = new BufferedReader(inputStreamReader);
    line = bufferedReader.readLine();
    while (line!=null) {
    jsonObject = parser.parse(line);
    lines.add(jsonObject);
    line = bufferedReader.readLine();
    }
    bufferedReader.close();
    inputStreamReader.close();
    } catch (Exception exception) {
    exception.printStackTrace();
    }
    return lines;
    }
    }
