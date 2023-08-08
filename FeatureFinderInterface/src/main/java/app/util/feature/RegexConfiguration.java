package app.util.feature;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RegexConfiguration {
    
   @Bean
   @Primary
   public RestTemplate restSimpleTemplate() {
     int connectionTimeout = 300000;
     int socketTimeout = 300000;

     SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
     factory.setConnectTimeout(connectionTimeout);
     factory.setReadTimeout(socketTimeout);
     return new RestTemplate(factory);
  }

  @LoadBalanced
  @Bean
  public RestTemplate restLoadBalancedTemplate() {
    int connectionTimeout = 300000;
    int socketTimeout = 300000;

    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(connectionTimeout);
    factory.setReadTimeout(socketTimeout);
    return new RestTemplate(factory);
  }

}
