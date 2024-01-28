package app.util.feature;

import javax.annotation.PostConstruct;

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
 
@RestController
public class SpellCheckingController { 

    private static String PROPERTIES_NAME="server.properties";
    private static final Logger logger = LoggerFactory.getLogger(SpellCheckingController.class);
    private SpellChecker spellChecker;
    
    @Autowired
	private RestTemplate restTemplate;
    @Autowired
	private WebApplicationContext applicationContext;
   
    @PostConstruct
    public void initialise() {
        String properties_location = System.getProperty(PROPERTIES_NAME);
        spellChecker = new SpellChecker();
    }	

    @RequestMapping(value = "/spellcheck", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<String> spellCheck(@RequestParam String text) {
	   Boolean success = false;
	   String response = "";
       if ((text!=null) && (text.length()>0)) {
          try {
                response = spellChecker.checkSpelling(text);
          } catch (Exception exception) {
                exception.printStackTrace();
          }
       }
	   return ResponseEntity.ok(response);
	}

}