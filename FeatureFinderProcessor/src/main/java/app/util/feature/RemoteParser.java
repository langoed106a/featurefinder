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

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import app.util.feature.Section;
import app.util.feature.WordToken;

import app.util.feature.ServiceLocator;

@Component
public class RemoteParser {
	private static String URL_PROPERTY_FILE="serverurls.properties";
	private ServiceLocator serviceLocator;
	private RestTemplate restTemplate;
    
	public RemoteParser() {
	}
	
	@PostConstruct
	private void init() {
	}
	
    public void setRestTemplate(RestTemplate restTemplate) {
		 this.restTemplate = restTemplate;
	} 
    
    public void setServiceLocator(ServiceLocator serviceLocator) {
		 this.serviceLocator = serviceLocator;
	}

	public String parseTextToText(String language, String text) {	
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<String> responseEntity = null;
		Section section = new Section();
		String destinationUrl = serviceLocator.getService(language);
		String result="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
		   destinationUrl = destinationUrl.replace("%1","parsetext");
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		   headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
           httpEntity = new HttpEntity<String>(text, headers);
		   responseEntity = restTemplate.postForEntity(destinationUrl, httpEntity, String.class); 
           result = (String)responseEntity.getBody();
		}
	return result;	   
   }

   public Boolean wordexists(String language, String listname, String text) {	
		Boolean result=false;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<Boolean> responseEntity = null;
		Section section = new Section();
		String destinationUrl = serviceLocator.getService(language);
		String urlencodedtext="";

		if (destinationUrl != null) {
           headers = new HttpHeaders();
		   destinationUrl = destinationUrl.replace("%1","wordexists?listname="+listname+",&word="+text);
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		   headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
           httpEntity = new HttpEntity<String>(text, headers);
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, Boolean.class); 
           result = (Boolean)responseEntity.getBody();	
     	}
	return result;	   
   }
	
}