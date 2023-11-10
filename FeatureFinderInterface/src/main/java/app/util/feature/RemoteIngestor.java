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

import app.util.feature.WordToken;
import app.util.feature.FeatureDocument;
import app.util.feature.HTTPAsyncSender;
import app.util.feature.ServiceLocator;
import app.util.feature.TokenGenerator;

@Component
public class RemoteIngestor {
    public static String SERVICE_NAME="batch";
	private static String PROCESS_DOCUMENTS="asyncprocessdocuments";
	private ServiceLocator serviceLocator;
	private DocumentDatabase documentDatabase;
	private HTTPAsyncSender httpAsyncSender;
	private RestTemplate restTemplate;
    
	public RemoteIngestor() {
	}
    
    public void setServiceLocator(ServiceLocator serviceLocator) {
		 this.serviceLocator = serviceLocator;
		 httpAsyncSender = new HTTPAsyncSender(serviceLocator);
	}
    
    public void setDatabase(DocumentDatabase documentDatabase) {
		 this.documentDatabase = documentDatabase;
	}
     
    public String runasyncgroupagainstdocument(String runname, String description, String language, String featuregroupname, String documentgroupname) {	
    	Document document = null;
		String response = "", tokenId = "";
		Map<String, String> params = null;
		try {
			  params = new HashMap<>();
			  tokenId = TokenGenerator.newToken();
              params.put("tokenid", tokenId);
			  params.put("documentgrouplist",documentgroupname);
			  params.put("featuregrouplist",featuregroupname);
			  response=httpAsyncSender.sendget(PROCESS_DOCUMENTS, params);
			  document = new Document("", runname, "run", tokenId, description);
			  this.documentDatabase.addDocument(document);
			  response="{\"message\":\"processing feature group against document group with tokenid:\"\""+tokenId+"\"}";
		} catch (Exception exception) {
			  exception.printStackTrace();
			  response="{\"error\":\"unable to process feature group against document group\"}";
		}
		return response;
    }

}