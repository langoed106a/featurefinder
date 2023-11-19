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

import org.asynchttpclient.Dsl;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.asynchttpclient.util.HttpConstants;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

import app.util.database.DocumentDatabase;

import app.util.feature.WordToken;
import app.util.feature.Document;
import app.util.feature.DocumentList;
import app.util.feature.ServiceLocator;

@Component
public class RemoteDatabase {
    public static String SERVICE_NAME="database";
	private ServiceLocator serviceLocator;
	private DocumentDatabase documentDatabase;
	private AsyncHttpClient httpClient;
	private RestTemplate restTemplate;
    
	public RemoteDatabase() {
		httpClient = new DefaultAsyncHttpClient();
	}
    
    public void setServiceLocator(ServiceLocator serviceLocator) {
		 this.serviceLocator = serviceLocator;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
    } 
     
    public List<Document> getDocumentByGroup(String groupname) {
    	HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		DocumentList documentList;
		List<Document> documents = new ArrayList<>();
		ResponseEntity<DocumentList> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
           destinationUrl = destinationUrl.replace("%1","getdocumentgroup?groupname="+groupname); 
           responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, DocumentList.class);
      	   documentList = responseEntity.getBody();
		   documents = documentList.getDocumentList();
		}
	  return documents;	
	}

	public Document getDocumentById(Integer id) {
		HttpEntity<String> httpEntity = null;
		Document document = null;
		ResponseEntity<Document> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
    	   destinationUrl = destinationUrl.replace("%1","getdocumentbyid?documentid="+id); 
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, Document.class);
    	   document = responseEntity.getBody();
		}
	  return document;	
	}

    public Document getDocumentByName(String name) {
		HttpEntity<String> httpEntity = null;
		Document document = null;
		ResponseEntity<Document> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
           destinationUrl = destinationUrl.replace("%1","getdocumentbyname?name="+name); 
    	   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, Document.class);
    	   document = responseEntity.getBody();
		}
	  return document;	
	}

	public List<Document> getDocumentByType(String type) {
		HttpEntity<String> httpEntity = null;
		DocumentList documentList;
		List<Document> documents = new ArrayList<>();
		ResponseEntity<DocumentList> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
 		   destinationUrl = destinationUrl.replace("%1","getdocumentsbytype?type="+type); 
           responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, DocumentList.class);
      	   documentList = responseEntity.getBody();
		   documents = documentList.getDocumentList();
		}
	  return documents;	
	}
	
	public String deleteDocument(String id) {
		String result="";
                return result;
	}

	public String updateDocument(Document document) {
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		HttpEntity<Document> httpEntity=null;
		HttpHeaders requestHeaders=null;
		ResponseEntity<String> responseEntity = null;
		if (destinationUrl != null) {
           destinationUrl = destinationUrl.replace("%1","updatedocument"); 
		   requestHeaders = new HttpHeaders();
		   requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		   requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		   httpEntity = new HttpEntity<>(document, requestHeaders);
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.POST, httpEntity, String.class);
      	   result = responseEntity.getBody();
		}
		return result;
	}
	
	public String addDocument(Document document) {
        String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		HttpEntity<Document> httpEntity=null;
		HttpHeaders requestHeaders=null;
		ResponseEntity<String> responseEntity = null;
		if (destinationUrl != null) {
           destinationUrl = destinationUrl.replace("%1","adddocument"); 
		   requestHeaders = new HttpHeaders();
		   requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		   requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		   httpEntity = new HttpEntity<>(document, requestHeaders);
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.POST, httpEntity, String.class);
      	   result = responseEntity.getBody();
		}
		return result;
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