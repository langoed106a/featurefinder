package app.util.feature;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
public class Documentation {
    private static String DOCUMENTATION_FILE="documentation.txt";
	private WebApplicationContext applicationContext;

	public Documentation() {
		
	}
	
	public String getDocumentation(WebApplicationContext applicationContext) {
		String docs = "";
		this.applicationContext = applicationContext;
		docs = this.readResource(DOCUMENTATION_FILE);
		return docs;
	}
	
	public String readResource(String filename) {
		BufferedReader bufferedReader=null;
		InputStream inputStream=null;
		InputStreamReader inputStreamReader = null;
		Resource resource=null;
		String line="", docs="";
		try {
			 resource = applicationContext.getResource("classpath:"+filename);
			 inputStream = resource.getInputStream();
			 inputStreamReader = new InputStreamReader(inputStream);
			 bufferedReader = new BufferedReader(inputStreamReader);
			 line = bufferedReader.readLine();
			 while (line!=null) {
				 docs = docs + line + "\n";
				 line = bufferedReader.readLine();
			 }
			 bufferedReader.close();
			 inputStreamReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return docs;
	}
	
	
	
	
}
