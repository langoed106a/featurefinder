package app.util.feature;

import javax.annotation.PostConstruct;

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

import io.swagger.annotations.ApiOperation;
 
@RestController
public class ChineseController { 
   private FeatureFunction featureFunction;
   private WordStorage wordStorage;

	@Autowired
	private ChineseParser chineseParser;
   @Autowired
	private WebApplicationContext applicationContext;
   
   @PostConstruct
   public void initialise() {
        featureFunction = new FeatureFunction();
        wordStorage = new WordStorage();
   }
		
	@RequestMapping(value = "/postagtext", method = RequestMethod.GET)
    public String postagtext(@RequestParam String text ) { 
	  Section section = chineseParser.parseTextToText(text);
	  List<WordToken> wordTokenList = section.getSentenceAtIndex(0);
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
		
	@RequestMapping(value = "/parsetext", method = RequestMethod.POST)
    public String parsetext(@RequestBody String text ) { 
	    Section section = chineseParser.parseTextToText(text);
      return section.toJson();
    }
    
    @RequestMapping(value = "/wordexists", method = RequestMethod.GET)
    public String wordexists(@RequestParam String listname, @RequestParam String word) { 
	    Boolean reply = wordStorage.wordExists(listname,word);
       return reply.toString();
    }
	 
}