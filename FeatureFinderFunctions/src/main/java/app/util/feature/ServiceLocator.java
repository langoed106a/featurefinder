package app.util.feature;

import java.util.Map;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ServiceLocator {

    private Map<String, String> serviceMap;

    public ServiceLocator(String filename) {
        serviceMap = this.loadUrls(filename);
    }

    public void addService(String name, String serviceUrl) {
        serviceMap.put(name, serviceUrl);
    }

    public String getService(String serviceName) {
         return serviceMap.get(serviceName);
    }
    
    private Map<String, String> loadUrls(String filename) {
		String line="";
		String urlKey="", url="";
		Map<String, String> urlMap = new HashMap<>();
		InputStreamReader inputStreamReader=null;
		Integer index=0,position=0;
		BufferedReader bufferedReader=null;
  	    try {
			 inputStreamReader = new InputStreamReader(new FileInputStream(filename), "UTF-8");
             bufferedReader = new BufferedReader(inputStreamReader);
		     line = bufferedReader.readLine();
	         index=1;
		     while (line!=null) {
	            //New index
				position = line.indexOf("=");
				if (position>0) {
	                urlKey = line.substring(0,position);
					url = line.substring(position+1, line.length()); 
	                urlMap.put(urlKey, url);
				}
	            line = bufferedReader.readLine();
	            index=index+1;
		     } 
			 bufferedReader.close();
  	    } catch (Exception exception) {
  	    	exception.printStackTrace();
  	    }
	  return urlMap;
	} 
}