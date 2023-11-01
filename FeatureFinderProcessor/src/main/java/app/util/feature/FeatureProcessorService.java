package app.util.feature;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.text.DateFormat;   
import java.util.Date;
import java.util.HashMap; 
import java.util.Map;

import java.net.URLEncoder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.util.feature.FeatureDocument;
 
@CrossOrigin
@RestController
public class FeatureProcessorService { 
	private static String RESULTS_LOCATION="/tmp";
	private static String DEFAULT_LANGUAGE="english";
	private static String DEFAULT_GRANULARITY="text";
	private static String PROPERTIES_NAME="server.properties";
	private static final Logger logger=LoggerFactory.getLogger(FeatureProcessorService.class);
	private ContractFunction contractFunction;
	private ConcurrentLinkedQueue<TextDocument> concurrentLinkedQueue;
	private FeatureFunction featureFunction;
	private RegexService regexService;
	private ServiceLocator serviceLocator;
	private WordStorage wordStorage;
	private TextDocument textToProcess;
	private Map<String, RegexDocumentList> regexMapList;
	private ObjectMapper objectMapper;
	private Integer lastStart;

	@Autowired
	private RestTemplate restSimpleTemplate;
	@Autowired
	private WebApplicationContext applicationContext;
    
	
	@PostConstruct
	public void initialise() {
		featureFunction = new FeatureFunction();
		String properties_location = System.getProperty(PROPERTIES_NAME);
		serviceLocator = new ServiceLocator(properties_location);
		contractFunction = new ContractFunction(featureFunction, wordStorage);
		regexService = new RegexService(serviceLocator);
		regexMapList = new HashMap<>();
		objectMapper = new ObjectMapper();
		wordStorage = new WordStorage();
		lastStart = 0;
		textToProcess=null;
	}
	
	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
    public String health() { 
	   return "healthy";
    }

	@RequestMapping(value = "/textdocument", method = RequestMethod.GET)
    public TextDocument getTextDocumentTemplate() { 
		TextDocument textDocument = new TextDocument();
	    return textDocument;
    }

	@RequestMapping(value = "/regexdocument", method = RequestMethod.GET)
    public RegexDocument getRegexDocumentTemplate() { 
	    RegexDocument regexDocument = new RegexDocument();
	    return regexDocument;
    }

	@RequestMapping(value = "/regexresults", method = RequestMethod.GET)
    public RegexResult getRegexResultTemplate() { 
	    RegexResult regexResult = new RegexResult();
	    return regexResult;
    }

	@RequestMapping(value = "/featureresults", method = RequestMethod.GET)
    public FeatureResult getFeatureResultTemplate() { 
	    FeatureResult featureResult = new FeatureResult();
	    return featureResult;
    }

	@RequestMapping(value = "/regexdocumentlist", method = RequestMethod.GET)
    public RegexDocumentList getRegexDocumentListTemplate() { 
	    RegexDocumentList documentList = new RegexDocumentList();
	    return documentList;
    }

	@RequestMapping(value = "/asyncprocesstext", method = RequestMethod.POST)
    public String asyncprocesstext(@RequestBody String text, @RequestParam String tokenid) { 
	   CompletableFuture<String> futureStr;
	   String status="{\"message\":\"received text\",\"status\":200}", response="";
	   RegexDocumentList regexDocumentList=null;
	   TextDocument textDocument=null;
	   TextDocument document=null;
	   try {
		    logger.info("Received new text document");
			textDocument = new TextDocument();
	        textDocument.fromJson(text);
			if (regexMapList.containsKey(tokenid)) {
			    regexDocumentList = regexMapList.get(tokenid);
				for (RegexDocument regexDocument:regexDocumentList.getRegexDocumentList()) {
				   regexDocument.setId(tokenid);
				   futureStr = regexService.doAsyncRegex(textDocument, regexDocument, featureFunction, wordStorage, contractFunction);
				   response = futureStr.get();
			    }
			}
	   } catch (Exception exception) {
		    exception.printStackTrace();
			status = "{\"error\":\""+exception.getMessage()+"\",\"status\":500}";
	   }
	   return status;
    }	

	@RequestMapping(value = "/syncprocesstext", method = RequestMethod.POST)
    public String syncprocesstext(@RequestBody String text, @RequestParam String tokenid) { 
	   CompletableFuture<FeatureResult> futureResult=null;
	   FeatureResult featureResult = null;
	   RegexDocumentList regexDocumentList=null;
	   String response = "";
	   TextDocument textDocument=null;
	   TextDocument document=null;
	   try {
		    logger.info("Received new text document");
			textDocument = new TextDocument();
	        textDocument.fromJson(text);
			if (regexMapList.containsKey(tokenid)) {
			    regexDocumentList = regexMapList.get(tokenid);
				for (RegexDocument regexDocument:regexDocumentList.getRegexDocumentList()) {
				   regexDocument.setId(tokenid);
				   futureResult= regexService.doSyncRegex(textDocument, regexDocument, featureFunction, wordStorage, contractFunction);
				   featureResult = futureResult.get();
				   featureResult.setSentenceList(textDocument.getSentenceList());
				   regexMapList.remove(tokenid);
				   response = featureResult.toJson();
			    }
			}
	   } catch (Exception exception) {
		    exception.printStackTrace();
			response = "{\"error\":\""+exception.getMessage()+"\",\"status\":500}";
	   }
	   return response;
    }	

	@RequestMapping(value = "/asyncprocessfeature", method = RequestMethod.POST)
    public String asyncProcessfeature(@RequestBody String featurelist, @RequestParam String tokenid) { 
	  Boolean valid = false, keepDocument = true;
	  CompletableFuture<String> futureStr = null;
	  FeatureResult featureResult = new FeatureResult();
	  RegexDocumentList regexDocumentList = new RegexDocumentList();
	  RegexDocument regexDocument = new RegexDocument();
	  RegexResult regexResult = null;
	  TextDocument textDocument = null;
	  String response="", label="", messageType="";
	  String[] parts=null;	
	  Integer matchcount = 0;
	  List<WordToken> tokens=null;
	  List<String> matches=null;
	  List<RegexResult> regexResultList = new ArrayList<>();
	  Matcher matcher = null;
	  Object object = null;
	  featureFunction.initialise();
	  // featureFunction.setFeatureStore(documentDatabase);
	  featureFunction.setWordStorage(wordStorage);
	  try {
		  regexDocumentList.fromJson(featurelist);
		  messageType = regexDocumentList.getMessageType();
		  response="{\"message\":\"message type "+messageType+" has been applied\",\"status\":200}";
		  if (messageType.equalsIgnoreCase("add")) {
			  if (!regexMapList.containsKey(tokenid)) {
				  regexMapList.put(tokenid, regexDocumentList);
			  } else {
				  response="{\"error\":\"regex feature list already present\",\"status\":500}";
			  } 
		  } else if (messageType.equalsIgnoreCase("remove")) {
			  if (regexMapList.containsKey(tokenid)) {
				  regexMapList.remove(tokenid);
			  } else {
				  response="{\"error\":\"regex feature list is not present\",\"status\":500}";
			  } 
		  } else if (messageType.equalsIgnoreCase("replace")) {
			  if (regexMapList.containsKey(tokenid)) {
				  regexMapList.replace(tokenid, regexDocumentList);
			  } else {
				  response="{\"error\":\"regex feature list is not present\",\"status\":500}";
			  } 
		  }
		} catch (Exception regexException) {
			response="{\"error\":\""+regexException.getMessage()+",\"status\":500}";
		}	
      return response;
    }
	
}