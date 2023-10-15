package app.util.feature;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.util.concurrent.Future;

public class HTTPAsyncSender {
    AsyncHttpClient httpClient;
    ResponseHandler responseHandler;

    public HTTPAsyncSender() {
       httpClient = new DefaultAsyncHttpClient();
    }

    public String send(String destination, String content) {
        BoundRequestBuilder builder=null;
        Future<String> reply=null;
        String message="";
        destination = destination.toLowerCase();
        if (destination.startsWith("http")) {
            builder =  httpClient.preparePost(destination);
            builder.addHeader("Content-type", "application/json;charset=utf-8");
            builder.addHeader("Accept", "application/json");
            builder.setBody(content);
            try {
                 reply = builder.execute(new ResponseHandler());
                 message = reply.get();
            } catch (Exception exception) {
                message = "500";
            }
        } else if (destination.startsWith("file")) {
            destination = destination.substring(0,7);
            try {
                 message = writeToFile(destination, content);
            } catch (Exception exception) {
                message="500";
            }
        }
        return message;
    }

    private String writeToFile(String destination, String text) throws Exception {
        File file = new File(destination);
        String message="200";
        Writer writer = null;
        PrintWriter out = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            out = new PrintWriter(writer);
            out.print(text+"\n");           
            out.flush();
            writer.flush();
        } finally {
            writer.close();
            out.close();
        }
        return message;
    }
}