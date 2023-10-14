package app.util.feature;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;

public class ResponseHandler extends AsyncCompletionHandler<String> {

    public ResponseHandler() {

    }

    @Override
    public String onCompleted(Response response) throws Exception {
        return "200";
    }
}