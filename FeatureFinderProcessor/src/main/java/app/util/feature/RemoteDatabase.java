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

import app.util.feature.Section;
import app.util.feature.WordToken;
import app.util.feature.FeatureDocument;
import app.util.feature.FeatureDocumentList;
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
     
    public List<FeatureDocument> getDocumentGroup(String groupname) {
    	HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		FeatureDocumentList featureDocumentList;
		List<FeatureDocument> documentList = new ArrayList<>();
		ResponseEntity<FeatureDocumentList> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
           destinationUrl = destinationUrl.replace("%1","getdocumentgroup?groupname="+groupname); 
           responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, FeatureDocumentList.class);
      	   featureDocumentList = responseEntity.getBody();
		   documentList = featureDocumentList.getFeatureDocumentList();
		}
	  return documentList;	
	}

	public FeatureDocument getDocumentById(String id) {
		HttpEntity<String> httpEntity = null;
		FeatureDocument document = null;
		ResponseEntity<FeatureDocument> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
    	   destinationUrl = destinationUrl.replace("%1","getdocumentbyid?documentid"+id); 
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, FeatureDocument.class);
    	   document = responseEntity.getBody();
		}
	  return document;	
	}

    public FeatureDocument getDocumentByName(String name, String type) {
		HttpEntity<String> httpEntity = null;
		FeatureDocument document = null;
		ResponseEntity<FeatureDocument> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
           destinationUrl = destinationUrl.replace("%1","getdocumentbyname?name="+name+"&type="+type); 
    	   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, FeatureDocument.class);
    	   document = responseEntity.getBody();
		}
	  return document;	
	}

	public List<FeatureDocument> getDocumentByType(String type) {
		HttpEntity<String> httpEntity = null;
		FeatureDocumentList featureDocumentList;
		List<FeatureDocument> documentList = new ArrayList<>();
		ResponseEntity<FeatureDocumentList> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           httpEntity = this.getHeaders();
 		   destinationUrl = destinationUrl.replace("%1","getdocumentsbytype?type="+type); 
           responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, FeatureDocumentList.class);
      	   featureDocumentList = responseEntity.getBody();
		   documentList = featureDocumentList.getFeatureDocumentList();
		}
	  return documentList;	
	}
	
	public String deleteDocument(String id) {
		String result="";
                return result;
	}

	public String updateDocument(Integer id, String name, String type, String contents, String description) {
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		HttpEntity httpEntity=null;
		ResponseEntity<String> responseEntity = null;
		if (destinationUrl != null) {
           destinationUrl = destinationUrl.replace("%1","updatedocument?id="+type+"&name="+name+"&type="+type+"&contents="+contents+"&description="+description); 
		   httpEntity = this.getHeaders();
		   responseEntity = restTemplate.exchange(destinationUrl, HttpMethod.GET, httpEntity, String.class);
      	   result = responseEntity.getBody();
		}
	  return result;
	}
	
	public synchronized String addDocument(String name, String type, String contents, String description) {
        String reply = "";
        return reply;
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