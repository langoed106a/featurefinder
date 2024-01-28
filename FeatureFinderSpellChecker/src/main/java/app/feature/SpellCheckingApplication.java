package app.util.feature;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "app.util.feature")
public class SpellCheckingApplication implements CommandLineRunner {
	
    public static void main(String[] args) {
        SpringApplication.run(SpellCheckingApplication.class, args);
    }

	@Bean
    public RestTemplate restTemplate() {
        int connectionTimeout = 240000;
	    int socketTimeout = 240000;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(socketTimeout);
        return new RestTemplate(factory);
    }
    
    @Override
	public void run(String... args) throws Exception {
		//Create the database table:
		//jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS featuredocumentstore(id INTEGER PRIMARY KEY,name TEXT,type TEXT,description TEXT,contents BLOB)");
		
		//Insert a record:
		//jdbcTemplate.execute("INSERT INTO beers VALUES ('Stella')");

		//Read records:
		// List<FeatureDocument> documents = jdbcTemplate.query("SELECT * FROM featuredocumentstore",(resultSet, rowNum) -> new FeatureDocument(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("type"),resultSet.getString("description"),resultSet.getString("contents")));
		
		//Print read records:
		// documents.forEach(System.out::println);
	}
}