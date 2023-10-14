package app.util.feature;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;

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
        return message;
    }

}