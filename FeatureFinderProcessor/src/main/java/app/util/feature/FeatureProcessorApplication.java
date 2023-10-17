package app.util.feature;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import app.util.feature.FeatureDocument;

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
public class FeatureProcessorApplication implements CommandLineRunner {

	@Bean
    public RestTemplate restTemplate() {
        int connectionTimeout = 240000;
	int socketTimeout = 240000;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(socketTimeout);
        return new RestTemplate(factory);
    }

    public static void main(String[] args) {
        SpringApplication.run(FeatureProcessorApplication.class, args);
    }
   
    @Override
	public void run(String... args) throws Exception {
	}
  
}