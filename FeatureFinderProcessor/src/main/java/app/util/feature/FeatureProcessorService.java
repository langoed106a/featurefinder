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
	private ServiceLocator serviceLocator;
	private WordStorage wordStorage;
	private TextDocument textToProcess;
	private ObjectMapper objectMapper;

	@Autowired
	private RestTemplate restSimpleTemplate;
	@Autowired
	private WebApplicationContext applicationContext;
	@Autowired
    private RegexService regexService;
	
	@PostConstruct
	public void initialise() {
		featureFunction = new FeatureFunction();
		String properties_location = System.getProperty(PROPERTIES_NAME);
		serviceLocator = new ServiceLocator(properties_location);
	    concurrentLinkedQueue = new ConcurrentLinkedQueue();
		contractFunction = new ContractFunction(featureFunction, wordStorage);
		objectMapper = new ObjectMapper();
		wordStorage = new WordStorage();
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

	@RequestMapping(value = "/processtext", method = RequestMethod.POST)
    public String processtext(@RequestBody String text) { 
	   String status="200";
	   try {
	        textToProcess = objectMapper.readValue(text, TextDocument.class);
			concurrentLinkedQueue.add(textToProcess);
	   } catch (Exception exception) {
		    textToProcess = null;
			status = "500";
	   }
	   return status;
    }

	@RequestMapping(value = "/syncprocessfeature", method = RequestMethod.POST)
    public FeatureResult syncProcessfeature(@RequestBody String body) { 
	  Boolean valid = false, showHighlight = false;
	  CompletableFuture<RegexResult> futureMatch = null;
	  FeatureResult featureResult = null;
	  RegexResult regexResult = null;
	  RegexDocument regexDocument = null;
	  String result="", matchesStr="", text="", tokensStr="", entry="", regex="", highlight="", language="", granularity="", precondition="", postcondition="", invariant="";
	  String[] parts=null;	
	  TextDocument textDocument=null;
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
		  featureResult = new FeatureResult();
		  regexDocument = objectMapper.readValue(body, RegexDocument.class);
	      if (regexDocument != null) {
			textDocument = this.getTextDocument();
			 if ((textDocument!=null) && (regexDocument!=null)) {
                    futureMatch = regexService.doSyncRegex(textDocument, regexDocument, featureFunction, wordStorage, contractFunction);
					regexResult = futureMatch.get();
					regexResultList.add(regexResult);
		    }
		  }
		} catch (Exception regexException) {
			regexException.printStackTrace();
		}	
      return featureResult;
    }

	@RequestMapping(value = "/asyncprocessfeature", method = RequestMethod.POST)
    public FeatureResult asyncProcessfeature(@RequestBody String body) { 
	  Boolean valid = false, showHighlight = false;
	  CompletableFuture<RegexResult> futureMatch = null;
	  FeatureResult featureResult = new FeatureResult();
	  RegexDocument regexDocument = null;
	  RegexResult regexResult = null;
	  TextDocument textDocument = null;
	  String result="", matchesStr="", text="", tokensStr="", entry="", regex="", highlight="", language="", granularity="", precondition="", postcondition="", invariant="";
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
		  regexDocument = objectMapper.readValue(body, RegexDocument.class);
	      if (regexDocument != null) {
			 textDocument = this.getTextDocument();
			 if ((textDocument!=null) && (regexDocument!=null)) {
					matches = null;
					regexResult = new RegexResult();
					futureMatch = regexService.doAsyncRegex(textDocument, regexDocument, featureFunction, wordStorage, contractFunction);
					regexResult = futureMatch.get();
					regexResultList.add(regexResult);
		    }
			result="200";
		  }
		} catch (Exception regexException) {
			result="500";
		}	
      return featureResult;
    }

	private TextDocument getTextDocument() {
		TextDocument textDocument = null;
		textDocument = concurrentLinkedQueue.poll();
		return textDocument;
	}
	
}