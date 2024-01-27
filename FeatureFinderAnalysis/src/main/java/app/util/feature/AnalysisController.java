package app.util.feature;

import javax.annotation.PostConstruct;

import app.util.database.DocumentDatabase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
 
import java.io.FileReader;
import java.io.BufferedReader;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.util.feature.Document;
import app.util.feature.RemoteDatabase;
import app.util.feature.WordList;
import app.util.feature.RegexResult;
 
@RestController
public class AnalysisController { 

    private static String PROPERTIES_NAME="server.properties";
    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);
    private ServiceLocator serviceLocator;
    private WekaModelGenerator wekaModelGenerator;
    private NativeWeighting nativeWeighting;
    
    @Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RemoteDatabase remoteDatabase;
    @Autowired
	private DocumentDatabase documentDatabase;
    @Autowired
	private WebApplicationContext applicationContext;
   
    @PostConstruct
    public void initialise() {
        String properties_location = System.getProperty(PROPERTIES_NAME);
        serviceLocator = new ServiceLocator(properties_location);
        remoteDatabase.setRestTemplate(restTemplate);
        remoteDatabase.setServiceLocator(serviceLocator);
	    documentDatabase.setRemoteDatabase(remoteDatabase);
        wekaModelGenerator = new WekaModelGenerator();
        nativeWeighting = new NativeWeighting();
    }	

    @RequestMapping(value = "/buildclassifierfromfile", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Boolean> buildClassifierFromFile(@RequestParam String modellocation) {
	   Boolean success = false;
	   String model = null;
       if ((modellocation!=null) && (modellocation.length()>0)) {
          try {
            model = this.readjsonModel(modellocation);
            success = this.createClassifierFromModel(model);
          } catch (Exception exception) {
                exception.printStackTrace();
          }
       }
	   return ResponseEntity.ok(success);
	}

    @RequestMapping(value = "/buildclassifierfrommodel", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Boolean> buildClassifierFromModel(@RequestParam String model) {
	    Boolean success = false;
        success = this.createClassifierFromModel(model);
	  return new ResponseEntity<>(success, HttpStatus.OK);
	}

    @RequestMapping(value = "/getresults", method = RequestMethod.GET, produces="application/json")
    public WordList getResults(@RequestParam String runname, @RequestParam String token, @RequestParam String model) {
	    Boolean success = false;
        Document document = null, outputDocument = null;
        List<String> resultList = new ArrayList<>(), modelListResults = new ArrayList<>();
        String path = "", filename="", results = "", docId="";
        WordList wordList = new WordList();
        if (runname != null) {
            document = documentDatabase.getDocumentByName(runname);
            if (document!=null) {
                filename = token;
                docId = document.getOrigin();
                outputDocument = documentDatabase.getDocumentByName(docId);
                if (outputDocument != null) {
                    path = outputDocument.getContents();
                    path = URLDecoder.decode(path);
                    if (!path.endsWith("/")) {
                       path = path + "/";
                    }
                    path = path + filename + ".dat";
                   resultList = this.readjsonFile(path);
                   if (model.equalsIgnoreCase("native_english")) {
                       results = nativeWeighting.getModelResults(resultList);
                       modelListResults.add(results);
                       wordList.setWordList(modelListResults);
                   } else {
                       wordList.setWordList(resultList);
                  }
                }
            }
        } 
	  return wordList;
	}

    @RequestMapping(value = "/getresultsbyid", method = RequestMethod.GET, produces="application/json")
    public WordList getResultsById(@RequestParam String runid, @RequestParam String model) {
	    Boolean success = false;
        Document document = null, outputDocument = null;
        List<String> resultList = new ArrayList<>(), modelListResults = new ArrayList<>();
        String path = "", filename="", results = "", docId="";
        WordList wordList = new WordList();
        if (runid != null) {
            document = documentDatabase.getDocumentById(runid);
            if (document!=null) {
                filename = document.getContents();
                filename = URLDecoder.decode(filename);
                docId = document.getOrigin();
                outputDocument = documentDatabase.getDocumentByName(docId);
                if (outputDocument != null) {
                    path = outputDocument.getContents();
                    path = URLDecoder.decode(path);
                    if (!path.endsWith("/")) {
                       path = path + "/";
                    }
                    path = "/"+path + filename + ".dat";
                   resultList = this.readjsonFile(path);
                   if (model.equalsIgnoreCase("native_english")) {
                       results = nativeWeighting.getModelResults(resultList);
                       modelListResults.add(results);
                       wordList.setWordList(modelListResults);
                   } else {
                       wordList.setWordList(resultList);
                  }
                }
            }
        } 
	  return wordList;
	}

    private Boolean createClassifierFromModel(String model) {
	    Boolean success = false;
        Document document=null;
        Integer intValue=0;
        JSONArray jsonArray = null, rowArray = null;
        JSONObject jsonObject = null;
        JSONParser jsonParser = new JSONParser();
        List<String> data = null, namesList = null;
        Object object=null;
        String contents="", classnames="", dataStr="", modelfilepath="", resultData="", row="", rowValue="", modelName="";
        String[] items=null;
        if ((model!=null) && (model.length()>0)) {
           try {
                resultData = General.decode(model);
                object = jsonParser.parse(resultData);
                if (object != null) {
                   jsonObject = (JSONObject)object;
                   modelName = (String)jsonObject.get("modelname");
                   jsonArray = (JSONArray)jsonObject.get("resultdata");
                   if (jsonArray != null) {
                      data = new ArrayList<>();
                      for (int i=0; i<jsonArray.size(); i++) {
                          rowArray = (JSONArray)jsonArray.get(i);
                          row="";
                          for (int j=0; j<rowArray.size(); j++) {
                              object = rowArray.get(j);
                              rowValue = String.valueOf(object);
                              row = row + rowValue+",";
                          }
                          if (row.length()>0) {
                             row=row.substring(0, row.length()-1);
                          }
                          data.add(row);
                       }
                       document = documentDatabase.getDocumentByName(modelName);
                       contents = document.getContents();
                       contents = URLDecoder.decode(contents);
                       object = jsonParser.parse(contents);
                       if (object!=null) {
                         namesList = new ArrayList<>();
                         jsonObject = (JSONObject)object;
                         modelfilepath = (String)jsonObject.get("file");
                         classnames = (String)jsonObject.get("classnames");
                         items = classnames.split(",");
                         for (int k=0; k<items.length; k++) {
                             namesList.add(items[k]);
                         }
                         dataStr = wekaModelGenerator.classify(modelfilepath, data, namesList, true);
                       }
                  }
                }
           } catch (Exception exception) {
                exception.printStackTrace();
           }
        }
	  return success;
	}

    private String readjsonModel(String name) {
        String jsonModel = "";
        return jsonModel;
    }

    private List<String> readjsonFile(String path) {
        FileReader reader = null;
        BufferedReader bufferedReader = null; 
        Integer valIndex = 0, valCount=0, count=0, regexCount=0;
        List<String> featureNames = new ArrayList<>();
        List<String> dataLines = new ArrayList<>();
        Map<String, Map<String, Integer>> fileMap = new HashMap<>();
        Map<String, Integer> featureMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        RegexResult regexResult = null;
        String regexName="", regexNumber="", textName="", row="", currentLine="";
        try {
              reader = new FileReader(path);
              bufferedReader = new BufferedReader(reader);
              while((currentLine=bufferedReader.readLine()) != null) {
                regexResult = mapper.readValue(currentLine, RegexResult.class);
                regexName = regexResult.getRegexName();
                regexCount = regexResult.getCount();
                textName = regexResult.getTextName();
                if (!(featureNames.contains(regexName))) {
                    featureNames.add(regexName);
                }
                if (fileMap.containsKey(textName)) {
                     featureMap = (Map) fileMap.get(textName);
                     if (featureMap.containsKey(regexName)) {
                        count = featureMap.get(regexName);
                        count = count + regexCount;
                        featureMap.replace(regexName, count);
                     } else {
                        featureMap.put(regexName, regexCount);
                     }
                } else {
                    featureMap = new HashMap<>();
                    featureMap.put(regexName, regexCount);
                    fileMap.put(textName, featureMap);
                }
              } 
        } catch (Exception exception) {
            logger.error("File at "+path+" doesn't exist");
        }
        row = "";
        for (String feature:featureNames) {
                row = row + feature + ",";
        }
        row = row.substring(0,row.length()-1);
        dataLines.add(row);
        row="";
        for (String key:fileMap.keySet()) {
           row = key + ",";
           featureMap = fileMap.get(key);
           for (String feature:featureNames) {
               regexCount = featureMap.get(feature);
               if (regexCount==null) {
                regexCount=0;
               }
               row = row + regexCount + ",";
           }
           row = row.substring(0,row.length()-1);
           dataLines.add(row);
        }
      return dataLines;
    }
	 
}