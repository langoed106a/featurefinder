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
import app.util.database.FeatureDocument;
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
		List<FeatureDocument> documentList = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","getdocumentgroup?groupname="+groupname); 
           //result = restTemplate.getForObject(destinationUrl, FeatureDocumentList.class);
	//	   documentList = result.getDocumentList();
		}
	  return documentList;	
	}

	public FeatureDocument getDocumentById(String id) {
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		FeatureDocument document = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","getdocumentbyid?documentid"+id); 
           document = restTemplate.getForObject(destinationUrl, FeatureDocument.class);
		}
	  return document;	
	}


        public FeatureDocument getDocumentByName(String name, String type) {
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		FeatureDocument document = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","getdocumentbyname?name="+name+"&type="+type); 
           document = restTemplate.getForObject(destinationUrl, FeatureDocument.class);
		}
	  return document;	
	}

        public Integer getNumberOfDocumentByType(String type) {
           Integer number = 0;
           return number;
        }

	public List<FeatureDocument> getDocumentByType(String type) {
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		List<FeatureDocument> documentList = null;
		//FeatureDocumentList featureDocumentList = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","getdocumentbytype?type="+type); 
           //featureDocumentList = restTemplate.getForObject(destinationUrl, FeatureDocumentList.class);
		//   documentList = featureDocumentList.getDocumentList();
		}
	  return documentList;	
	}

	
	public String deleteDocument(String id) {
		String result="";
                return result;
	}

	public String updateDocument(Integer id, String name, String type, String contents, String description) {
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		List<FeatureDocument> documentList = null;
		// FeatureDocumentList featureDocumentList = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","updatedocument?id="+type+"&name="+name+"&type="+type+"&contents="+contents+"&description="+description); 
           result = restTemplate.getForObject(destinationUrl, String.class);
		}
	  return result;
	}
	
	public synchronized String addDocument(String name, String type, String contents, String description) {
        String reply = "";
        return reply;
	}

    public String doHTTPCall(String destinationUrl, String url) {	
    	String response="";
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
    	destinationUrl = destinationUrl.replace("%1",url);    
		if (destinationUrl != null) {
			headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpEntity = new HttpEntity<String>(headers);
			try {	 
			       response = restTemplate.getForObject(destinationUrl, String.class);
		    } catch (Exception exception) {
			       exception.printStackTrace();
		    }	
		 }
       return response;
    }
	
}