package app.util.feature;


import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Component;

import app.util.feature.ServiceLocator;

@Component
public class IncomingCallHandler {
    private static String THREADS_PER_QUEUE="threadsperinqueue";
    private List<ProcessIncomingThread> queueThreadList;
    private NodeServer nodeServer;
    private OutgoingCallHandler outgoingCallHandler;
    private ServiceLocator serviceLocator;
    private CallProcessor callProcessor;
    private ConcurrentLinkedQueue<String> textQueue;

    public IncomingCallHandler() {
    }

    public void initialise(ServiceLocator serviceLocator, NodeServer nodeServer, CallProcessor callProcessor, OutgoingCallHandler outgoingCallHandler) {
        this.textQueue = new ConcurrentLinkedQueue<>();
        this.callProcessor = callProcessor;
        this.serviceLocator = serviceLocator;
        this.outgoingCallHandler = outgoingCallHandler;
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
       ProcessIncomingThread queueThread = null;
       for (int number=0; number<numberOfThreads; number++) {
          queueThread = new ProcessIncomingThread(this.textQueue, this.callProcessor, this.outgoingCallHandler);
          this.queueThreadList.add(queueThread);
          queueThread.start();
       }
    }

    private String getServerProperty(String propertyName) {
       String propertyValue = serviceLocator.getService(propertyName); 
       return propertyValue;
    }
}