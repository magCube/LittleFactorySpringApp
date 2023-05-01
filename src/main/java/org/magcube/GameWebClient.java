package org.magcube;

import org.magcube.dto.GameCreatedResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GameWebClient {

  private final WebClient client;

  // Spring Boot auto-configures a `WebClient.Builder` instance with nice defaults and customizations.
  // We can use it to create a dedicated `WebClient` for our component.
  public GameWebClient(WebClient.Builder builder) {
    this.client = builder.baseUrl("http://localhost:8080").build();
  }

  public Mono<String> getMessage() {
    return this.client.post().uri("/game").accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(GameCreatedResponse.class)
        .map(GameCreatedResponse::getMessage);
  }

}
