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
public class RemoteBatch {
    public static String SERVICE_NAME="batch";
	private ServiceLocator serviceLocator;
	private DocumentDatabase documentDatabase;
	private AsyncHttpClient httpClient;
	private RestTemplate restTemplate;
    
	public RemoteBatch() {
		httpClient = new DefaultAsyncHttpClient();
	}
    
    public void setServiceLocator(ServiceLocator serviceLocator) {
		 this.serviceLocator = serviceLocator;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
    } 
    
    public void setFeatureStore(DocumentDatabase documentDatabase) {
		 this.documentDatabase = documentDatabase;
	}
     
    public String runsyncgroupagainstdocument(String runname, String description, String language, String featuregroupname, String documentgroupname) {	
    	String destinationUrl = serviceLocator.getService(SERVICE_NAME); 
    	String response="";
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
    	destinationUrl = destinationUrl.replace("%1","runsyncgroupagainstdocument?runname="+runname+"&description="+description+"&language="+language+"&groupname="+featuregroupname+"&documentname="+documentgroupname);    
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

	public String runasyncgroupagainstdocument(String runname, String description, String language, String featuregroupname, String documentgroupname) {	
    	String destinationUrl = serviceLocator.getService(SERVICE_NAME); 
    	String reply="batch job sent";
    	destinationUrl = destinationUrl.replace("%1","runasyncgroupagainstdocument?runname="+runname+"&description="+description+"&language="+language+"&groupname="+featuregroupname+"&documentname="+documentgroupname);    
    	Request request = new RequestBuilder(HttpConstants.Methods.GET).setUrl(destinationUrl).build();
    	Response response = null;
    	Future<Response> responseFuture = httpClient.executeRequest(request);
    	try {
    	      response = responseFuture.get();
    	} catch (Exception exception) {
    		exception.printStackTrace();
    		reply="Batch run failed";
    	}
       return reply;
    }
    
}