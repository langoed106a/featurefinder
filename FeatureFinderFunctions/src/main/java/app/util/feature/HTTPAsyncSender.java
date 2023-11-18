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
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class HTTPAsyncSender {
    AsyncHttpClient httpClient;
    ResponseHandler responseHandler;
    ServiceLocator serviceLocator;

    public HTTPAsyncSender(ServiceLocator serviceLocator) {
       httpClient = new DefaultAsyncHttpClient();
       this.serviceLocator = serviceLocator;
    }

    public String send(String documentType, String content) {
        BoundRequestBuilder builder=null;
        Future<String> reply=null;
        String message="500", destination="";
        destination = serviceLocator.getService(documentType);
        if (destination !=null) {
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
                destination = destination.substring(7, destination.length());
                if ((destination!=null) && (destination.length()>0)) {
                    try {
                         message = writeToFile(destination, content);
                    } catch (Exception exception) {
                        message="500";
                    }
                } else {
                    message="500";
                }
            }
        }
        return message;
    }

    public String sendpost(String documentType, String content, Map<String, String> params) {
        BoundRequestBuilder builder=null;
        Future<String> reply=null;
        String message="500", destination="";
        List<String> paramList = null;
        Map<String, List<String>> queryParams=null;
        destination = serviceLocator.getService(documentType);
        if (destination !=null) {
            if (params!=null) {
                queryParams = new HashMap<>();
                for (String key:params.keySet()) {
                    paramList = new ArrayList<>();
                    paramList.add(params.get(key));
                    queryParams.put(key, paramList);
                }
            }
            if (destination.startsWith("http")) {
                builder =  httpClient.preparePost(destination);
                builder.addHeader("Content-type", "application/json;charset=utf-8");
                builder.addHeader("Accept", "application/json");
                builder.setBody(content);
                builder.setQueryParams(queryParams);
                try {
                     reply = builder.execute(new ResponseHandler());
                     message = reply.get();
                } catch (Exception exception) {
                    message = "500";
                }
            } else if (destination.startsWith("file")) {
                destination = params.get("param1");
                if (destination.endsWith("/")) {

                } else {
                    destination = destination + File.separator + params.get("param2") + ".dat";
                }
                if ((destination!=null) && (destination.length()>0)) {
                    try {
                         message = writeToFile(destination, content);
                    } catch (Exception exception) {
                        message="500";
                    }
                } else {
                    message="500";
                }
            }
        }
        return message;
    }

    public String sendget(String documentType, Map<String, String> params) {
        BoundRequestBuilder builder=null;
        Future<String> reply=null;
        String message="500", destination="";
        List<String> paramList = null;
        Map<String, List<String>> queryParams=null;
        destination = serviceLocator.getService(documentType);
        if (destination !=null) {
            if (destination.startsWith("http")) {
                builder =  httpClient.prepareGet(destination);
                builder.addHeader("Content-type", "application/json;charset=utf-8");
                builder.addHeader("Accept", "application/json");
                if (params!=null) {
                    queryParams = new HashMap<>();
                    for (String key:params.keySet()) {
                        paramList = new ArrayList<>();
                        paramList.add(params.get(key));
                        queryParams.put(key, paramList);
                    }
                    builder.setQueryParams(queryParams);
                }

                try {
                     reply = builder.execute(new ResponseHandler());
                     message = reply.get();
                } catch (Exception exception) {
                    message = "500";
                }
            } 
        }
        return message;
    }

    public String send(String documentType, String content, String name) {
        BoundRequestBuilder builder=null;
        Future<String> reply=null;
        Integer position=0;
        String message="500", destination="";
        destination = serviceLocator.getService(documentType);
        if (destination !=null) {
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
                destination = destination.substring(7, destination.length());
                if ((destination!=null) && (destination.length()>0)) {
                    try {
                         position = destination.indexOf('.');
                         if ((position>0) && (name!=null) && (name.length()>0)) {
                            destination=name+"."+destination.substring(position+1, destination.length());
                         }
                         message = writeToFile(destination, content);
                    } catch (Exception exception) {
                        message="500";
                    }
                } else {
                    message="500";
                }
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