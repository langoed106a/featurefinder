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

import app.util.database.DocumentDatabase;
import app.util.feature.FeatureDocument;
 
@CrossOrigin
@RestController
public class FeatureProcessorService { 
	private static String RESULTS_LOCATION="/tmp";
	private static String DEFAULT_LANGUAGE="english";
	private static String DEFAULT_GRANULARITY="text";
	private static String PROPERTIES_NAME="server.properties";
	private static final Logger logger=LoggerFactory.getLogger(RegexService.class);
	private ContractFunction contractFunction;
	private FeatureFunction featureFunction;
	private ServiceLocator serviceLocator;
	private WordStorage wordStorage;
	private TextDocument textToProcess;
	private ObjectMapper objectMapper;

	@Autowired
	private RestTemplate restSimpleTemplate;
	@Autowired
	private RemoteBatch remoteBatch;
	@Autowired
	private RemoteDatabase remoteDatabase;
	@Autowired
	DocumentDatabase documentDatabase;
	@Autowired
	private WebApplicationContext applicationContext;
	
	@PostConstruct
	public void initialise() {
		featureFunction = new FeatureFunction();
		String properties_location = System.getProperty(PROPERTIES_NAME);
		serviceLocator = new ServiceLocator(properties_location);
	
		remoteParser.setRestTemplate(restSimpleTemplate);
		remoteParser.setServiceLocator(serviceLocator);

		remoteDatabase.setRestTemplate(restSimpleTemplate);
		remoteDatabase.setServiceLocator(serviceLocator);

		contractFunction = new ContractFunction(featureFunction, wordStorage);
		documentDatabase.setRemoteDatabase(remoteDatabase);
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
	   textToProcess = objectMapper.readValue(body, TextDocument.class);
	   return "200";
    }

	@RequestMapping(value = "/processfeature", method = RequestMethod.POST)
    public String processfeature(@RequestBody String body) { 
	  Boolean valid = false, showHighlight = false;
	  RegexDocument regexDocument = null;
	  String result="", matchesStr="", text="", tokensStr="", entry="", regex="", highlight="", language="", granularity="", precondition="", postcondition="", invariant="";
	  String[] parts=null;	
	  Integer matchcount = 0;
	  List<WordToken> tokens=null;
	  Matcher matcher = null;
	  Object object = null;
	  featureFunction.initialise();
	  featureFunction.setFeatureStore(documentDatabase);
	  featureFunction.setWordStorage(wordStorage);
	  try {
		  regexDocument = objectMapper.readValue(body, RegexDocument.class);
	      if (regexDocument != null) {
			 if ((textToProcess!=null) && (regexDocument!=null)) {
					matches = null;
					matcher = new Matcher(regexDocument, featureFunction, wordStorage, contractFunction);
					if (showHighlight) {
					   matches = matcher.matchtext(textDocument);
					} else {
						 matchcount = matcher.matchcount(textDocument);
					}
	                section.toSingleSentence();
                    tokens = section.getCurrentSentence();
	                for (WordToken wordToken:tokens) {
		               tokensStr = tokensStr + wordToken.toJson()+",";
	                }
			        if (tokensStr.endsWith(",")) {
				       tokensStr = tokensStr.substring(0, tokensStr.length()-1);
			        }		 
                    tokensStr = "\"tokens\":"+"[" + tokensStr + "]";
	                for (String match:matches) {
                       parts = match.split(":");
		               entry = "{\"start\":\""+parts[0]+"\",\"end\":\""+parts[1]+"\"},";
		               matchesStr = matchesStr + entry;
	                }
	                if (matchesStr.length()>0) {
	                   matchesStr = matchesStr.substring(0,matchesStr.length()-1);
	                   matchesStr = "\"matches\":"+"[" + matchesStr + "]";
	                } else {
		               matchesStr = "\"matches\":"+"[" + "]";
	                }	   
	            result = "{"+tokensStr+","+matchesStr+"}";
		        }
			  }  
		    }
		} catch (Exception regexException) {
			result = "{\"error\":\""+regexException.getMessage()+"\"}";
		}	
      return result;
    }
	
}