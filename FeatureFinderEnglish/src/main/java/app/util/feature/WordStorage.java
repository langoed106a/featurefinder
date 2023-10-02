package app.util.feature;

import java.io.BufferedReader;
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
    
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser; 
    
public class WordStorage {
   private BigInteger[] commonHashWords;
   private Map<String, List<String>> wordsMap;
   private WordSearch wordSearch;
   private PostagSearch postagSearch;
       
   public WordStorage() {
        this.wordSearch = new WordSearch();
        this.postagSearch = new PostagSearch();
        wordsMap = new HashMap<>();
        this.readWordLists();
    }

    public Map<String, String> getNameofWordLists() {
        Map<String,String> wordListMap = new HashMap<>();
        List<String> nameList = new ArrayList<>(wordsMap.keySet());
        for (String name:nameList) {
            wordListMap.put(name,"words from an internal word list");
        }
      return wordListMap;
    }

    public void readWordLists() {
       List<String> wordsList=new ArrayList<>();
       List<String> fileList=new ArrayList<>();
       Path path = null;
       String listname="";

       URL url = WordStorage.class.getClassLoader().getResource("/");
       try {
            path = Paths.get(url.toURI());
            Files.walk(path, 5).forEach(p -> fileList.add(p.toString()));
       } catch (Exception exception) {
            exception.printStackTrace();
       }
       for (String str: fileList) {
            wordsList = new ArrayList<>();
            wordsList = this.readResource(str);
            listname = str.replace(".txt","");
            wordsMap.put(listname, wordsList);
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
   
    public List<String> readResource(String filename) {
    BufferedReader bufferedReader=null;
    InputStream inputStream=null;
    InputStreamReader inputStreamReader = null;
    List<String> wordsList = new ArrayList<>();
    String line="";
    try {
    inputStream = WordStorage.class.getClassLoader().getResourceAsStream(filename);
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
