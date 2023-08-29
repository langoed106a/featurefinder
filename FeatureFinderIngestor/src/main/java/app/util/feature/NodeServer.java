package app.util.feature;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeServer {
   private String url;
   private Boolean busy;

   @Autowired
   WebClient webClient;
   
   public NodeServer(String url) {
      this.url = url;
      this.busy = false;
   }

   public void finished() {
      this.busy = false;
   }

   public void startJob() {
      this.busy = true;
   }

   public CompletableFuture<String> makeHttpCall(HttpMethod httpMethod, String url, String textBlock, Map<String, String> headers) {
       return webClient.post()
            .uri(url)
            .body(Mono.just(textBlock), String.class)
            .headers(httpHeaders -> headers.forEach(httpHeaders::add))
            .retrieve()
            .bodyToMono(String.class)
            // specify timeout
            .timeout(Duration.ofSeconds(5L))
            // subscribe on a different thread from the given scheduler to avoid blocking as toFuture is a subscriber
            .subscribeOn(Schedulers.single())
            // subscribes to the mono and converts it to a completable future
            .toFuture();
   }

}
