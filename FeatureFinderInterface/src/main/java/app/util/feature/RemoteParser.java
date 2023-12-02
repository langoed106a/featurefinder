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

import app.util.feature.WordToken;
import app.util.feature.DocumentList;
import app.util.feature.WordList;

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

   public Boolean wordexists(String language, String listname, String word) {	
		Boolean result=false;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<Boolean> responseEntity = null;
		String destinationUrl = serviceLocator.getService(language);
		String urlencodedtext="";
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
		   destinationUrl = destinationUrl.replace("%1","wordexists?listname="+listname+",&word="+word);
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, Boolean.class); 
           result = (Boolean)responseEntity.getBody();	
     	}
	return result;	   
   }

   public Boolean listexists(String language, String listname) {	
		Boolean result=false;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<Boolean> responseEntity = null;
		String destinationUrl = serviceLocator.getService(language);
		String urlencodedtext="";
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
		   destinationUrl = destinationUrl.replace("%1","listexists?listname="+listname);
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, Boolean.class); 
           result = (Boolean)responseEntity.getBody();	
     	}
	return result;	   
   }

   public String addList(String language, Document document) {
        String destinationUrl = serviceLocator.getService(language);
		String result="";
		HttpEntity<Document> httpEntity=null;
		HttpHeaders requestHeaders=null;
		ResponseEntity<String> responseEntity = null;
		if (destinationUrl != null) {
           destinationUrl = destinationUrl.replace("%1","addlist"); 
		   requestHeaders = new HttpHeaders();
		   requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		   requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		   httpEntity = new HttpEntity<>(document, requestHeaders);
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.POST, httpEntity, String.class);
      	   result = responseEntity.getBody();
		}
		return result;
	}

    public Document getList(String language, String documentId) {
        String destinationUrl = serviceLocator.getService(language);
		Document result=null;
		HttpEntity<String> httpEntity=null;
		HttpHeaders requestHeaders=null;
		ResponseEntity<Document> responseEntity = null;
		if (destinationUrl != null) {
		   httpEntity = this.getHeaders();
           destinationUrl = destinationUrl.replace("%1","wordlistbyid?id="+documentId); 
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, Document.class);
      	   result = responseEntity.getBody();
		}
		return result;
	}

   public List<String> listnames(String language) {	
		WordList result=null;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		List<String> nameList = null;
		ResponseEntity<WordList> responseEntity = null;
		String destinationUrl = serviceLocator.getService(language);
		String urlencodedtext="";
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
		   destinationUrl = destinationUrl.replace("%1","listnames");
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, WordList.class); 
           result = responseEntity.getBody();	
		   nameList = result.getWordList();
     	}
	return nameList;	   
   }

   public List<Document> getDocuments(String language, String type) {
	    List<Document> allDocuments = new ArrayList<>();
		DocumentList documentList = null;
	    HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<DocumentList> responseEntity = null;
		String destinationUrl = serviceLocator.getService(language);
		String urlencodedtext="";
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
		   destinationUrl = destinationUrl.replace("%1","documentbytype?type="+type);
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, DocumentList.class); 
           documentList = responseEntity.getBody();	
		   allDocuments = documentList.getDocumentList();
     	}
	return allDocuments;
   }

   private HttpEntity getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity httpEntity = null;
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpEntity = new HttpEntity<String>(headers);
		return httpEntity; 
	}
	
}
