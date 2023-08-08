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
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import app.util.database.DocumentDatabase;
import app.util.feature.NodeServer;

public class NodeController {
     List<NodeServer> nodeServerList;   
     ConcurrentLinkedQueue<String> queue; 
     AsyncHttpClient client;

     public NodeController(String[] serverUrls) {
	nodeServerList = new ArrayList<NodeServer>();
        queue = new ConcurrentLinkedQueue<>();
        client = new DefaultAsyncHttpClient();
        for (int i=0; i<serverUrls.length; i++) {
           queue.add(serverUrls[i]);
        } 
     }

    public void startJobs(String documentgroupname) {
       // get all documents
       Boolean finished = false;
       String serverId = "", document="";
       while (!finished) {
           serverId = (String)queue.poll();
           document = this.getDocument();
           if ((document !=null) && (document.length()>0)) {
              this.callServerNode(serverId, document);
           } else {
              finished = true;
           }
       }
    }   

   public void callServerNode(String serverId, String document) {
      String entity = document;
      String reply ="";
      Future<String> response = client.preparePost(serverId)
                                      .addHeader("Content-Type", "application/json")
                                      .setBody(entity)
                                      .execute(new AsyncFinishedHandler(serverId, queue));
     }

   private String getDocument() {
     String document="";
     return document;
   }
}