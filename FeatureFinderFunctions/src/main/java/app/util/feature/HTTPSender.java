package app.util.feature;

import org.asynchttpclient.AsyncHttpClient;

public class HTTPSender {
    AsyncHttpClient c = new AsyncHttpClient();
    Future<Response> f = c.prepareGet(TARGET_URL).execute();

   public HTTPSender() {
     httpClient = new AsyncHttpClient();
   }

   public String send() {

   }

}