package app.util.feature;

import java.util.Arrays;

import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class HTTPSyncSender {
    RestTemplate restTemplate;

    public HTTPSyncSender(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
    }

    public String send(String destination, String content) {	
		HttpHeaders headers = null;
		HttpEntity<String> httpEntity = null;
		ResponseEntity<String> responseEntity = null;
		String result="";
		if (destination != null) {
           httpEntity = this.getHeaders(content);
	       responseEntity = restTemplate.exchange(destination, HttpMethod.GET, httpEntity, String.class); 
           result = (String)responseEntity.getBody();
		}
	return result;
    }

    private HttpEntity getHeaders(String content) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity httpEntity = null;
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpEntity = new HttpEntity<String>(content, headers);
		return httpEntity; 
	}


   }