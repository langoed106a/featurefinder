package app.util.feature;

import javax.annotation.PostConstruct;

import app.util.database.DocumentDatabase;


import app.util.feature.Sentence;
import app.util.feature.ServiceLocator;
import app.util.feature.FeatureDocument;
import app.util.feature.HTTPAsyncSender;
import app.util.feature.TextDocument;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.ApiOperation;
 
@RestController
public class EnglishController { 
   private static final Logger logger=LoggerFactory.getLogger(EnglishController.class);
   private static String PROPERTIES_NAME="server.properties";
   private EnglishParser englishParser;
   private FeatureFunction featureFunction;
   private HTTPSyncSender syncSender;
   private HTTPAsyncSender asyncSender;
   private ObjectMapper objectMapper;
   private ServiceLocator serviceLocator;
   private WordStorage wordStorage;

	@Autowired
	private DocumentDatabase documentDatabase;
	@Autowired
	private RestTemplate restTemplate;
    @Autowired
	private WebApplicationContext applicationContext;
	
   @PostConstruct
   public void initialise() {
	    String properties_location = System.getProperty(PROPERTIES_NAME);
        featureFunction = new FeatureFunction();
		syncSender = new HTTPSyncSender(restTemplate);
        serviceLocator = new ServiceLocator(properties_location);
		asyncSender = new HTTPAsyncSender(serviceLocator);
		wordStorage = new WordStorage(applicationContext);
		englishParser = new EnglishParser(applicationContext, wordStorage);
		objectMapper = new ObjectMapper();
   }
		
	@RequestMapping(value = "/syncparsetext", method = RequestMethod.POST)
    public String syncParseText(@RequestBody String text ) { 
		String jsonStr="";
	    TextDocument textDocument = null; 
		try {
			  textDocument = englishParser.parseText(text);
		      jsonStr = textDocument.toJson();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
      return jsonStr;
    }

	@RequestMapping(value = "/asyncparsetext", method = RequestMethod.POST)
    public String asyncParseText(@RequestBody String text ) { 
	    String jsonStr="", response="", uniqueId="", reply="";
		TextDocument textDocument = null;
        try {
		     textDocument = englishParser.parseText(text);
			 uniqueId = UUID.randomUUID().toString();
			 textDocument.setId(uniqueId);
		     jsonStr = textDocument.toJson();
			 reply = asyncSender.send("parsedtext", jsonStr);
			 response = "{\"token\":\""+uniqueId+"\",\"status\":200}";
		} catch (Exception exception) {
			exception.printStackTrace();
			response = "{\"error\":\""+exception.getMessage()+"\",\"status\":500}";
		}
	    return response;
    }

	@RequestMapping(value = "/asyncprocesstext", method = RequestMethod.POST)
    public String asyncProcessText(@RequestBody String text, @RequestParam String tokenid ) { 
	    String jsonStr="", response="", uniqueId="", reply="";
		Map<String, String> params = new HashMap<>();
		TextDocument textDocument = null;
        try {
		     textDocument = englishParser.parseText(text);
			 textDocument.setId(tokenid);
			 params.put("tokenid", tokenid);
		     jsonStr = textDocument.toJson();
			 reply = asyncSender.sendpost("processedtext", jsonStr, params);
			 response = "{\"token\":\""+tokenid+"\",\"status\":200}";
		} catch (Exception exception) {
			exception.printStackTrace();
			response = "{\"error\":\""+exception.getMessage()+"\",\"status\":500}";
		}
	    return response;
    }
    
    @RequestMapping(value = "/wordexists", method = RequestMethod.GET)
    public Boolean wordexists(@RequestParam String listname, @RequestParam String word) { 
	    Boolean reply = wordStorage.wordExists(listname,word);
       return reply;
    }

	@RequestMapping(value = "/sentences", method = RequestMethod.GET)
	public String sentences(@RequestParam String text) {
		List<String> lines = englishParser.getSentences(text);
		String jsonString = "{\"sentences:\"[";
		for (String line: lines) {
			jsonString = jsonString + "{\"" + line + "\"},";
		}
		if (jsonString.length()>15) {
			jsonString = jsonString.substring(0, jsonString.length()-1);
		}
		jsonString = jsonString + "]}";
		return jsonString;
	}
 
}