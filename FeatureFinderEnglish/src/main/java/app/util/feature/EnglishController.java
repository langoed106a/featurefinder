package app.util.feature;

import javax.annotation.PostConstruct;

import app.util.database.DocumentDatabase;
import app.util.feature.Document; 
import app.util.feature.DocumentList;
import app.util.feature.WordList;

import app.util.feature.Sentence;
import app.util.feature.ServiceLocator;
import app.util.feature.TokenGenerator;
import app.util.feature.FeatureDocument;
import app.util.feature.HTTPAsyncSender;
import app.util.feature.TextDocument;

import org.springframework.jdbc.core.JdbcTemplate;
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
	private JdbcTemplate jdbcTemplate;
    @Autowired
	private WebApplicationContext applicationContext;
	
   @PostConstruct
   public void initialise() {
	    String properties_location = System.getProperty(PROPERTIES_NAME);
        featureFunction = new FeatureFunction();
		syncSender = new HTTPSyncSender(restTemplate);
        serviceLocator = new ServiceLocator(properties_location);
		asyncSender = new HTTPAsyncSender(serviceLocator);
		documentDatabase.setJdbcTemplate(jdbcTemplate);
		wordStorage = new WordStorage(documentDatabase);
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
			 uniqueId = TokenGenerator.newToken();
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
    public String asyncProcessText(@RequestBody String text, @RequestParam String runname, @RequestParam String name) { 
	    String jsonStr="", response="", uniqueId="", reply="";
		Map<String, String> params = new HashMap<>();
		TextDocument textDocument = null;
        try {
		     textDocument = englishParser.parseText(text);
			 textDocument.setId(runname);
			 textDocument.setName(name);
			 params.put("runname", runname);
		     jsonStr = textDocument.toJson();
			 reply = asyncSender.sendpost("processedtext", jsonStr, params);
			 response = "{\"run\":\""+runname+"\",\"status\":200}";
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

	@RequestMapping(value = "/listexists", method = RequestMethod.GET)
    public Boolean listexists(@RequestParam String listname) { 
        Boolean exists = false;
        exists = wordStorage.listExists(listname);
        return exists;
    }

	@RequestMapping(value = "/listnames", method = RequestMethod.GET)
    public WordList listnames() { 
        Boolean exists = false;
		List<String> wordNameList = null;
		WordList wordList = new WordList();
        wordNameList = wordStorage.getNameofWordLists();
		wordList.setWordList(wordNameList);
        return wordList;
    }

	@RequestMapping(value = "/wordlistbyname", method = RequestMethod.GET)
    public Document getWordListByName(String listname) { 
        Document document = null;
        document = wordStorage.getWordListByName(listname);
        return document;
    }

	@RequestMapping(value = "/wordlistbyid", method = RequestMethod.GET)
    public Document getWordListById(String id) { 
        Document document = null;
        document = wordStorage.getWordListById(id);
        return document;
    }

	@RequestMapping(value = "/documentbytype", method = RequestMethod.GET)
    public DocumentList getDocumentByType(String type) { 
        List<Document> documentset = null;
		DocumentList documentList = new DocumentList();
        documentset = wordStorage.getDocumentByType(type);
		if (documentset!=null) {
			documentList.setDocumentList(documentset);
		}
        return documentList;
    }


	@RequestMapping(value = "/addlist", method = RequestMethod.POST)
    public Boolean addlist(@RequestBody Document document) { 
        Boolean exists = false;
        exists = wordStorage.addList(document);
        return exists;
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