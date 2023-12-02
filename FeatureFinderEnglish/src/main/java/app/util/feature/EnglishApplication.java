package app.util.feature;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"app.util.feature","app.util.database"})
public class EnglishApplication implements CommandLineRunner {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public static void main(String[] args) {
        SpringApplication.run(EnglishApplication.class, args);
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
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS featuredocumentstore(id INTEGER PRIMARY KEY autoincrement,name TEXT,type TEXT,contents BLOB,description TEXT,label TEXT,origin TEXT)");
		jdbcTemplate.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_documentstore_name ON featuredocumentstore (name)");
		//Print read records:
		// documents.forEach(System.out::println);
	}
}
