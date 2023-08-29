package app.util.feature;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Component;

@Component
public class CallHandler {
    private Thread queueThread;
    private ConcurrentLinkedQueue<TextBlock> textQueue;


    public CallHandler() {
        textQueue = new ConcurrentLinkedQueue();
    }

    public String handleCall(TextBlock textBlock) {
       String reply="";
       textQueue.add(textBlock);
       return reply;
    }

    public String processCall(Object object) {
       String reply="";
       TextBlock textBlock=null;
       if (object instanceof TextBlock) {
          textBlock = (TextBlock)object;
          System.out.println("***TEXTBLOCK****"+textBlock.toString());
       }
       System.out.println("***Processing Call*****");
       return reply;
    }


    public void pollQueue() {
       this.queueThread = new Thread(() -> {
            while (!textQueue.isEmpty()) {
                // consume element
                Object element = textQueue.poll();
                this.processCall(element);
                // do something with element
                // here
            }
        });
        this.queueThread.start();
    }
}