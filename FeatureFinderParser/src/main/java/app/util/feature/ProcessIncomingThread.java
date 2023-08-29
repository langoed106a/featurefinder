package app.util.feature;

import java.util.concurrent.ConcurrentLinkedQueue;


public class ProcessIncomingThread extends Thread {
    private ConcurrentLinkedQueue<String> textQueue;
    private OutgoingCallHandler outgoingCallHandler;
    private CallProcessor callProcessor;

    public ProcessIncomingThread(ConcurrentLinkedQueue<String> textQueue, CallProcessor callProcessor, OutgoingCallHandler outgoingCallHandler) {
        this.callProcessor = callProcessor;
        this.outgoingCallHandler = outgoingCallHandler;
        this.textQueue = textQueue;
    }

    public void run(){
       while (!textQueue.isEmpty()) {
            // consume element
            String element = (String)textQueue.poll();
            String result = this.callProcessor.processCall(element);
            this.outgoingCallHandler.handleCall(result);
        }
    }
  }