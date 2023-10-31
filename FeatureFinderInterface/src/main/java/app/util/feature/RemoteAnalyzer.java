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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.util.feature.WordToken;

import app.util.feature.ServiceLocator;

@Component
public class RemoteAnalyzer {
    public static String SERVICE_NAME="analyzer";
	private ServiceLocator serviceLocator;
	private RestTemplate restTemplate;
	private static final Logger logger = LoggerFactory.getLogger(RemoteAnalyzer.class);
    
	public RemoteAnalyzer() {
	}
	
    public void setRestTemplate(RestTemplate restTemplate) {
		 this.restTemplate = restTemplate;
	} 
    
    public void setServiceLocator(ServiceLocator serviceLocator) {
		 this.serviceLocator = serviceLocator;
	} 

    public String addRun(String runname, String description, String language, String featuregroupname, String documentgroupname, String resultslocation) {	
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","addrun?runname="+runname+"&description="+description+"&language="+language+"&featuregroupname="+featuregroupname+"&documentgroupname="+documentgroupname+"&resultslocation="+resultslocation); 
           result = restTemplate.getForObject(destinationUrl, String.class);
		}
	  return result;	   
    }

    public String getRuns() {	
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","getallruns"); 
           result = restTemplate.getForObject(destinationUrl, String.class);
		}
	  return result;	   
    }


	public String getResults(String runid, String model) {	
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","getresults?runid="+runid+"&model="+model); 
           result = restTemplate.getForObject(destinationUrl, String.class);
		}
	return result;	   
   }

    public String getModels() { 	
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String result="";
		String urlencodedtext="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","getmodels"); 
           result = restTemplate.getForObject(destinationUrl, String.class);
		}
	return result;	   
   }

    public boolean buildClassifier(String model) {
		Boolean response = false;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<Boolean> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String urlencodedText="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","buildclassifier"); 
		   try {
                urlencodedText = URLEncoder.encode(model, "UTF-8");
				responseEntity = restTemplate.postForEntity(destinationUrl, urlencodedText, Boolean.class);
		        response = (Boolean) responseEntity.getBody();
		   } catch(Exception exception) {
			   logger.error("trying to build classifier failed");
		   }
		}
	  return response;
	}

	public boolean buildClassifierFromModel(String model) {
		Boolean response = false;
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<Boolean> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String urlencodedText="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","buildclassifierfrommodel"); 
		   try {
                urlencodedText = URLEncoder.encode(model, "UTF-8");
				responseEntity = restTemplate.postForEntity(destinationUrl, urlencodedText, Boolean.class);
		        response = (Boolean) responseEntity.getBody();
		   } catch(Exception exception) {
			   logger.error("trying to build classifier from model failed");
		   }
		}
	  return response;
	}

	public String classifyData(String model) {
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<String> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String urlencodedText="", response="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","classifydata"); 
		   try {
                urlencodedText = URLEncoder.encode(model, "UTF-8");
				responseEntity = restTemplate.postForEntity(destinationUrl, urlencodedText, String.class);
		        response = (String) responseEntity.getBody();
		   } catch(Exception exception) {
			   logger.error("trying to build classifier from model failed");
		   }
		}
	  return response;
	}

	public String confusionMatrix(String data) {
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<String> responseEntity = null;
		String destinationUrl = serviceLocator.getService(SERVICE_NAME);
		String urlencodedText="", response="";
		if (destinationUrl != null) {
           headers = new HttpHeaders();
           headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
           httpEntity = new HttpEntity<String>(headers);
		   destinationUrl = destinationUrl.replace("%1","confusionmatrix"); 
		   try {
                urlencodedText = URLEncoder.encode(data, "UTF-8");
				responseEntity = restTemplate.postForEntity(destinationUrl, urlencodedText, String.class);
		        response = (String) responseEntity.getBody();
		   } catch(Exception exception) {
			   logger.error("Failed to get confusion matrix");
		   }
		}
	  return response;
	}
}
