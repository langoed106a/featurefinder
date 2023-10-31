package app.util.feature;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.text.DateFormat;   
import java.util.Date; 


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import app.util.database.DocumentDatabase;
import app.util.feature.HTTPSyncSender;
import app.util.feature.HTTPAsyncSender;
import app.util.feature.RegexDocument;
import app.util.feature.RegexDocumentList;
import app.util.feature.TextDocument;
 
@Component
public class RemoteProcessor { 
	private static String SERVICE_SYNC_PARSER="syncparser";
	private static String SERVICE_ASYNC_PROCESSOR="syncprocess";
	private static String PROPERTIES_NAME="server.properties";
	private static final Logger logger=LoggerFactory.getLogger(RemoteProcessor.class);
	private HTTPSyncSender httpSyncSender;
	private HTTPAsyncSender httpAsyncSender;
	private RestTemplate restTemplate;
	private ServiceLocator serviceLocator;

	public RemoteProcessor() {

	}
	
	public void setServiceLocator(ServiceLocator serviceLocator) {
		 this.serviceLocator = serviceLocator;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		httpSyncSender = new HTTPSyncSender(restTemplate);
		httpAsyncSender = new HTTPAsyncSender(serviceLocator);
    } 

	public String processText(String text, String tokenid) {
		String destinationUrl = serviceLocator.getService(SERVICE_SYNC_PARSER);
		String response = "";
		try {
			  response=httpSyncSender.send(destinationUrl, text);
		} catch (Exception exception) {
			  exception.printStackTrace();
		}
		return response;
	}
	
	public String processFeature(RegexDocumentList regexDocumentList, String tokenid) {
		String destinationUrl = serviceLocator.getService(SERVICE_ASYNC_PROCESSOR);
		String response = "";
		try {
			  response=httpAsyncSender.send(destinationUrl, regexDocumentList.toJson());
		} catch (Exception exception) {
			  exception.printStackTrace();
		}
		return response;
	}	
	
}