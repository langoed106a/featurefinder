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
import java.util.HashMap;
import java.util.Map;


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
	private static String DEFAULT_LANGUAGE="english";
	private static String SERVICE_SYNC_PARSER="syncparsetext";
	private static String SERVICE_ASYNC_PROCESS_FEATURE="asyncprocessfeature";
	private static String SERVICE_ASYNC_PROCESS_TEXT="asyncprocesstext";
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

	public String processText(String text, String runname) {
		Map<String, String> params=null;
		String languageUrl = serviceLocator.getService(SERVICE_SYNC_PARSER);
		String processorUrl = serviceLocator.getService(SERVICE_ASYNC_PROCESS_TEXT);
		String response = "";
		try {
            params.put("runname", runname);
			response=httpAsyncSender.sendpost(SERVICE_ASYNC_PROCESS_TEXT, text, params);
		} catch (Exception exception) {
			  exception.printStackTrace();
		}
		return response;
	}
	
	public String processFeature(RegexDocumentList regexDocumentList, String runname) {
		String response = "";
		Map<String, String> params = null;
		try {
			  params = new HashMap<>();
              params.put("runname", runname);
			  response=httpAsyncSender.sendpost(SERVICE_ASYNC_PROCESS_FEATURE, regexDocumentList.toJson(), params);
		} catch (Exception exception) {
			  exception.printStackTrace();
		}
		return response;
	}	
	
}