package app.util.feature;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import app.util.feature.ServiceLocator;

@Component
public class OutgoingCallHandler {
    private static String SERVICE_NAME="parser";
    private static String THREADS_PER_QUEUE="threadsperoutqueue";
    private List<ProcessOutgoingThread> queueThreadList;
    private Map<String, String> headers;
    private NodeServer nodeServer;
    private ServiceLocator serviceLocator;
    private ConcurrentLinkedQueue<String> textQueue;

    public OutgoingCallHandler() {
    }

    public void initialise(ServiceLocator serviceLocator, NodeServer nodeServer) {
        textQueue = new ConcurrentLinkedQueue<>();
        this.serviceLocator = serviceLocator;
        this.nodeServer = nodeServer;
        this.headers.put("Content-Type", "application/json");
        this.pollQueue();
    }

    public String handleCall(String content) {
       String reply="";
       this.textQueue.add(content);
       return reply;
    }
 
    public void pollQueue() {
       String threadsPerQueue=this.getServerProperty(THREADS_PER_QUEUE);
       Integer numberOfThreads=Integer.valueOf(threadsPerQueue);
       ProcessOutgoingThread queueThread = null;
       String uri = this.getServerProperty(SERVICE_NAME);   
       for (int number=0; number<numberOfThreads; number++) {
          queueThread = new ProcessOutgoingThread(this.textQueue, this.nodeServer, uri, this.headers);
          this.queueThreadList.add(queueThread);
          queueThread.start();
       }
    }

    private String getServerProperty(String propertyName) {
       String propertyValue = serviceLocator.getService(propertyName); 
       return propertyValue;
    }
}