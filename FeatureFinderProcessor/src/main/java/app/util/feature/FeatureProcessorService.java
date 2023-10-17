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
	private FeatureFunction featureFunction;
	private ServiceLocator serviceLocator;
	private WordStorage wordStorage;
	private TextDocument textToProcess;
	private ObjectMapper objectMapper;

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
		objectMapper = new ObjectMapper();
		textToProcess=null;
	}
	
	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
    public String health() { 
	   return "healthy";
    }

	@RequestMapping(value = "/processtext", method = RequestMethod.POST)
    public String processtext(@RequestBody String text) { 
	   textToProcess = objectMapper.readValue(text, TextDocument.class);
	   return "200";
    }

	@RequestMapping(value = "/syncprocessfeature", method = RequestMethod.POST)
    public ResultDocument syncProcessfeature(@RequestBody String body) { 
	  Boolean valid = false, showHighlight = false;
	  RegexDocument regexDocument = null;
	  ResultDocument resultDocument = null;
	  String result="", matchesStr="", text="", tokensStr="", entry="", regex="", highlight="", language="", granularity="", precondition="", postcondition="", invariant="";
	  String[] parts=null;	
	  Integer matchcount = 0;
	  List<WordToken> tokens=null;
	  List<String> matches=null;
	  Matcher matcher = null;
	  Object object = null;
	  featureFunction.initialise();
	  // featureFunction.setFeatureStore(documentDatabase);
	  featureFunction.setWordStorage(wordStorage);
	  try {
		  resultDocument = new ResultDocument();
		  regexDocument = objectMapper.readValue(body, RegexDocument.class);
	      if (regexDocument != null) {
			 if ((textToProcess!=null) && (regexDocument!=null)) {
					matches = null;
					matcher = new Matcher(regexDocument, featureFunction, wordStorage, contractFunction);
					if (showHighlight) {
					   matches = matcher.matchtext(textToProcess);
					} else {
						 matchcount = matcher.matchcount(textToProcess);
					}
					resultDocument.setSentenceList(textToProcess.getSentenceList());
	                resultDocument.setMatches(matches);
		        } 
		    }
		} catch (Exception regexException) {
			regexException.printStackTrace();
		}	
      return resultDocument;
    }

	@RequestMapping(value = "/asyncprocessfeature", method = RequestMethod.POST)
    public String asyncProcessfeature(@RequestBody String body) { 
	  Boolean valid = false, showHighlight = false;
	  RegexDocument regexDocument = null;
	  ResultDocument resultDocument = null;
	  String result="", matchesStr="", text="", tokensStr="", entry="", regex="", highlight="", language="", granularity="", precondition="", postcondition="", invariant="";
	  String[] parts=null;	
	  Integer matchcount = 0;
	  List<WordToken> tokens=null;
	  List<String> matches=null;
	  Matcher matcher = null;
	  Object object = null;
	  featureFunction.initialise();
	  // featureFunction.setFeatureStore(documentDatabase);
	  featureFunction.setWordStorage(wordStorage);
	  try {
		  resultDocument = new ResultDocument();
		  regexDocument = objectMapper.readValue(body, RegexDocument.class);
	      if (regexDocument != null) {
			 if ((textToProcess!=null) && (regexDocument!=null)) {
					matches = null;
					matcher = new Matcher(regexDocument, featureFunction, wordStorage, contractFunction);
					if (showHighlight) {
					   matches = matcher.matchtext(textToProcess);
					} else {
						 matchcount = matcher.matchcount(textToProcess);
					}
					resultDocument.setSentenceList(textToProcess.getSentenceList());
	                resultDocument.setMatches(matches);
		        } 
		    }
			result="200";
		} catch (Exception regexException) {
			result="500";
		}	
      return result;
    }
	
}