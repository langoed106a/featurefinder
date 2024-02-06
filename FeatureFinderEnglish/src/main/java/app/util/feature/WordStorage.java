package app.util.feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
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
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
    
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.util.database.DocumentDatabase;
    
public class WordStorage {
   private static final Logger logger=LoggerFactory.getLogger(WordStorage.class);
   private static String WORD_LIST_FOLDER="wordlists";
   private static String DICTIONARY="commonword";
   private static String WORD_FILE_EXTENSION=".dat";
   private WebApplicationContext applicationContext;
   private BigInteger[] commonHashWords;
   private Map<String, List<String>> wordsMap;
   private DocumentDatabase documentDatabase;
   private PostagSearch postagSearch;
       
   public WordStorage(DocumentDatabase documentDatabase, WebApplicationContext applicationContext) {
        List<String> wordList = null;
        this.documentDatabase = documentDatabase;
        this.applicationContext = applicationContext;
        wordsMap = new HashMap<>();
        wordList = this.loadResource(DICTIONARY+".dat");
        wordsMap.put(DICTIONARY, wordList);
    }

    public List<String> getNameofWordLists() {
        List<String> nameList = documentDatabase.getDocumentNameList();
        return nameList;
    }
    
    public Boolean addList(Document document) {
        Boolean added = false;
        documentDatabase.addDocument(document);
        return added;
    }

    public Document getWordListByName(String listname) {
        Document document = null;
        document = documentDatabase.getDocumentByName(listname);
        return document;
    }

    public Document getWordListById(String id) {
        Document document = null;
        Integer idInt = Integer.valueOf(id);
        document = documentDatabase.getDocumentById(idInt);
        return document;
    }

    public List<Document> getDocumentByType(String type) {
        List<Document> documentList = null;
        documentList = documentDatabase.getDocumentByType(type);
        return documentList;
    }

    public boolean wordExists(String listname, String word) {
        Boolean exists = false;
        Document document = null;
        Integer index=0;
        ObjectMapper objectMapper = null;
        String contents="", value="";
        List<String> listType = null;
        if (listname.equalsIgnoreCase(DICTIONARY)) {
            listType = wordsMap.get(DICTIONARY);
            word = word.toLowerCase();
            if (listType.contains(word)) {
                exists = true;
            }
        } else {
           document = documentDatabase.getDocumentByName(listname);
           if (document != null) {
               contents = document.getContents();
               try {
                  contents=URLDecoder.decode(contents);
                  objectMapper = new ObjectMapper();
                  listType = objectMapper.readValue(contents, new TypeReference<List<String>>(){});
                  if (listType != null) {
                     while ((!exists) && (index<listType.size())) {
                         value = listType.get(index);
                         if (word.equalsIgnoreCase(value)) {
                            exists = true;
                         }
                        index++;
                     }
                  }
               } catch (Exception exception) {
                   exception.printStackTrace();
               }
           }
        }
        return exists;
    }

    public boolean listExists(String listname) {
        Boolean exists = false;
        Document document = null;
        if (listname.equalsIgnoreCase(DICTIONARY)){
            exists = true;
        } else {
            document = documentDatabase.getDocumentByName(listname);
            if (document != null) {
                exists = true;
            }
        }
    return exists;
    }

    public List<String> loadResource(String filename) {
        List<String> contents = new ArrayList<>();
        String line="";
        BufferedReader bufferedReader=null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        try {
            //File file = ResourceUtils.getFile("classpath:wordlists/"+filename);
            inputStream = inputStream = applicationContext.getResource("classpath:wordlists/"+filename).getInputStream();
            //InputStream inputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            line = bufferedReader.readLine();
            while (line!=null) {
                if ((line!=null) && (line.length()>0)) {
                   contents.add(line);
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return contents;
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
