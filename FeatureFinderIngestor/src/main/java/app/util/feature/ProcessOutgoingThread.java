package app.util.feature;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.http.HttpMethod;

public class ProcessOutgoingThread extends Thread {
    private ConcurrentLinkedQueue<String> textQueue;
    private Map<String, String> headers;
    private NodeServer nodeServer;
    private CallProcessor callProcessor;
    private String outgoingUri;

    public ProcessOutgoingThread(ConcurrentLinkedQueue<String> textQueue, NodeServer nodeServer, String uri, Map<String, String> headers) {
        this.nodeServer = nodeServer;
        this.textQueue = textQueue;
        this.outgoingUri = uri;
        this.headers = headers;
    }

    public void run(){
       CompletableFuture<String> future;
       String uri;
       while (!textQueue.isEmpty()) {
            // consume element
            String element = (String)textQueue.poll();   
            try {
                 future = nodeServer.makeHttpCall(HttpMethod.POST, outgoingUri, element, headers);
                 future.get();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
  }