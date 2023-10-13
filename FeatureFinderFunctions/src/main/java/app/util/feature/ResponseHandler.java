package app.util.feature;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;

public class ResponseHandler<String> extends AsyncCompletionHandler<T> {

    public ResponseHandler() {

    }

    @Override
    public Object onCompleted(Response response) throws Exception {
        return response;
    }
}