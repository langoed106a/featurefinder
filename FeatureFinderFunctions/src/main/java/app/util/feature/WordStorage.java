package app.util.feature;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.MediaType;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import app.util.feature.TextDocument;
import app.util.feature.WordToken;
import app.util.feature.ServiceLocator;

@Component
public class WordStorage {
    public static String SERVICE_NAME="english";
	private ServiceLocator serviceLocator;
	private RestTemplate restTemplate;
    
	public WordStorage() {
	}
    
    public void setServiceLocator(ServiceLocator serviceLocator) {
		 this.serviceLocator = serviceLocator;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
    } 
    
    public Boolean wordExists(String word, String listname) {	
    	String destinationUrl = serviceLocator.getService(SERVICE_NAME); 
    	Boolean response=false;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
    	destinationUrl = destinationUrl.replace("%1","wordexists?listname="+listname+"&word="+word);    
		if (destinationUrl != null) {
			headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpEntity = new HttpEntity<String>(headers);
			try {	 
			       response = restTemplate.getForObject(destinationUrl, Boolean.class);
		    } catch (Exception exception) {
			       exception.printStackTrace();
		    }	
		 }
       return response;
    }

	public List<String> getList(String listname) {	
    	String destinationUrl = serviceLocator.getService(SERVICE_NAME), contents=""; 
		List<String> nameList=null;
    	Boolean response=false;
		Document document=null;
		HttpHeaders headers = null;
		ResponseEntity<Document> responseEntity = null;
		HttpEntity<String> httpEntity = null;
		ObjectMapper objectMapper = null;
    	destinationUrl = destinationUrl.replace("%1","wordlistbyname?listname="+listname);    
		if (destinationUrl != null) {
			headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			httpEntity = new HttpEntity<String>(headers);
			try {	 
                   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, Document.class); 
                   document = responseEntity.getBody();
				   if (document != null) {
					  contents = document.getContents();
					  try {
						    contents=URLDecoder.decode(contents);
							objectMapper = new ObjectMapper();
							nameList = objectMapper.readValue(contents, new TypeReference<List<String>>(){});
					  } catch (Exception exception) {
						  exception.printStackTrace();
					  }
				   }
		    } catch (Exception exception) {
			       exception.printStackTrace();
		    }	
		 }
       return nameList;
    }

	public Boolean addList(Document document) {	
    	String destinationUrl = serviceLocator.getService(SERVICE_NAME), list = ""; 
    	Boolean response=false;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
    	destinationUrl = destinationUrl.replace("%1","addlist?listname="+document.getName());    
		if (destinationUrl != null) {
			headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpEntity = new HttpEntity<String>(headers);
			try {	 
			       response = restTemplate.getForObject(destinationUrl, Boolean.class);
		    } catch (Exception exception) {
			       exception.printStackTrace();
		    }	
		 }
       return response;
    }

	public Boolean listExists(String listname) {	
    	String destinationUrl = serviceLocator.getService(SERVICE_NAME); 
    	Boolean response=false;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
    	destinationUrl = destinationUrl.replace("%1","listexists?listname="+listname);    
		if (destinationUrl != null) {
			headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpEntity = new HttpEntity<String>(headers);
			try {	 
			       response = restTemplate.getForObject(destinationUrl, Boolean.class);
		    } catch (Exception exception) {
			       exception.printStackTrace();
		    }	
		 }
       return response;
    }
    
}