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
	private RegexService regexService;
	private ServiceLocator serviceLocator;
	private WordStorage wordStorage;
	private TextDocument textToProcess;
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
	    concurrentLinkedQueue = new ConcurrentLinkedQueue();
		contractFunction = new ContractFunction(featureFunction, wordStorage);
		regexService = new RegexService(serviceLocator);
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

	@RequestMapping(value = "/processtext", method = RequestMethod.POST)
    public String processtext(@RequestBody String text) { 
	   String status="200";
	   TextDocument textDocument=null;
	   TextDocument document=null;
	   try {
		    logger.info("Received new text document");
			textDocument = new TextDocument();
	        textDocument.fromJson(text);
			concurrentLinkedQueue.add(textDocument);
	   } catch (Exception exception) {
		    exception.printStackTrace();
		    textToProcess = null;
			status = "500";
	   }
	   return status;
    }

	@RequestMapping(value = "/syncprocessfeature", method = RequestMethod.POST)
    public String syncProcessfeature(@RequestBody String feature) { 
	  Boolean valid = false, showHighlight = false;
	  CompletableFuture<FeatureResult> futureMatch = null;
	  FeatureResult featureResult = null;
	  RegexDocument regexDocument = new RegexDocument();
	  String result="";
	  TextDocument textDocument=null;
	  List<String> matches=null;
	  List<RegexResult> regexResultList = new ArrayList<>();
	  Matcher matcher = null;
	  featureFunction.initialise();
	  // featureFunction.setFeatureStore(documentDatabase);
	  featureFunction.setWordStorage(wordStorage);
	  try {
		  regexDocument.fromJson(feature);
	      if (regexDocument != null) {
			textDocument = this.getTextDocument(false);
			 if ((textDocument!=null) && (regexDocument!=null)) {
                    futureMatch = regexService.doSyncRegex(textDocument, regexDocument, featureFunction, wordStorage, contractFunction);
					featureResult = futureMatch.get();
				    result = featureResult.toJson();
		    }
		  }
		} catch (Exception regexException) {
			regexException.printStackTrace();
			result="{\"error\":\""+regexException.getMessage()+"\"}";
		}	
      return result;
    }

	@RequestMapping(value = "/asyncprocessfeature", method = RequestMethod.POST)
    public String asyncProcessfeature(@RequestBody String featurelist) { 
	  Boolean valid = false, keepDocument = true;
	  CompletableFuture<String> futureStr = null;
	  FeatureResult featureResult = new FeatureResult();
	  RegexDocumentList regexDocumentList = new RegexDocumentList();
	  RegexDocument regexDocument = new RegexDocument();
	  RegexResult regexResult = null;
	  TextDocument textDocument = null;
	  String response="", label="";
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
	      if (regexDocument != null) {
		      label = regexDocument.getLabel();
			  keepDocument = this.checkLabel(label);
			  textDocument = this.getTextDocument(keepDocument);
			  if ((textDocument!=null) && (regexDocument!=null)) {
				  futureStr = regexService.doAsyncRegex(textDocument, regexDocument, featureFunction, wordStorage, contractFunction);
				  response = futureStr.get();
			  }
		  }
		} catch (Exception regexException) {
			response="{\"error\":\""+regexException.getMessage()+",\"status\":500}";
		}	
      return response;
    }

	private TextDocument getTextDocument(Boolean keep) {
		TextDocument textDocument = null;
		if (keep) {
		    textDocument = concurrentLinkedQueue.peek();
		} else {
			textDocument = concurrentLinkedQueue.poll();
		}
		return textDocument;
	}

	private Boolean checkLabel(String label) {
		Boolean keep = false;
		Integer position=0, first=0, last=0;
		String start="", end="";
	    if ((label !=null) && (label.length()>0)) {
			position=label.indexOf("of");
			if (position>0) {
				start = label.substring(0,position);
				end = label.substring(position+2, label.length());
				first = Integer.valueOf(start);
				last = Integer.valueOf(end);
				if (first>this.lastStart) {
					if (first==last) {
						this.lastStart=0;
					} else if (first<last) {
                        this.lastStart=first;
						keep=true;
					}
				} 
			}
		}
		return keep;
	}
	
}