package app.util.feature;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;

import java.util.concurrent.Future;

public class HTTPSender {
    AsyncHttpClient httpClient;
    ResponseHandler responseHandler;

    public HTTPSender() {
       httpClient = new DefaultAsyncHttpClient();
    }

    public String send(String destination, String content) {
        BoundRequestBuilder builder=null;
        builder =  httpClient.preparePost(destination);
        builder.addHeader("Content-type", "application/json;charset=utf-8");
        builder.addHeader("Accept", "application/json");
        builder.setBody(content);
         Future<String> reply = builder.execute(new ResponseHandler());
    }

}