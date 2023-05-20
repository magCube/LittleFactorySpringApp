package org.magcube.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class PublisherConfig {

  @Bean
  public Sinks.Many<String> eventPublisher() {
    return  Sinks.many().unicast().onBackpressureBuffer();
  }

  @Bean
  public Flux<String> events(Sinks.Many<String> eventPublisher) {
    return eventPublisher
        .asFlux()
        .replay(25)
        .autoConnect();
  }

}
