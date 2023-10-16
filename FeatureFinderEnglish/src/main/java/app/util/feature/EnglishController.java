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
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.ApiOperation;
 
@RestController
public class EnglishController { 
   private static final Logger logger=LoggerFactory.getLogger(EnglishController.class);
   private static String PROPERTIES_NAME="server.properties";
   private FeatureFunction featureFunction;
   private HTTPSyncSender syncSender;
   private HTTPAsyncSender asyncSender;
   private ServiceLocator serviceLocator;
   private WordStorage wordStorage;

	@Autowired
	private EnglishParser englishParser;
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
   }

		
	@RequestMapping(value = "/postagtext", method = RequestMethod.GET)
    public String postagtext(@RequestParam String text ) { 
	  TextDocument textDocument = englishParser.parseText(text);
	  List<WordToken> wordTokenList = textDocument.getSentence(0);
	  String reply = "[";
	  for (WordToken wordToken:wordTokenList) {
		  reply = reply + "{";
		  reply = reply + "\"token\":";
		  reply = reply + "\""+wordToken.getToken()+"\",";
		  reply = reply + "\"lemma\":";
		  reply = reply + "\""+wordToken.getLemma()+"\",";
		  reply = reply + "\"postag\":";
		  reply = reply + "\""+wordToken.getPostag()+"\",";
		  reply = reply + "\"dependency\":";
		  reply = reply + "\""+wordToken.getDependency()+"\"";
		  reply = reply + "},";
	  }
	  reply = reply.substring(0,reply.length()-1);
	  reply = reply + "]";
      return reply;
    }
		
	@RequestMapping(value = "/syncparsetext", method = RequestMethod.POST)
    public String syncParseText(@RequestBody String text ) { 
	    TextDocument textDocument = englishParser.parseText(text);
      return textDocument.toJson();
    }

	@RequestMapping(value = "/asyncparsetext", method = RequestMethod.POST)
    public String asyncParseText(@RequestBody String text ) { 
	    String jsonText="", response="";
		TextDocument textDocument = englishParser.parseText(text);
        jsonText = textDocument.toJson();
		response = asyncSender.send("parsedtext", jsonText);
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