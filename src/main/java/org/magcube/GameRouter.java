package org.magcube;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class GameRouter {

  @Bean
  public RouterFunction<ServerResponse> route(GameHandler gameHandler) {

    return RouterFunctions
        .route(POST("/game").and(accept(MediaType.APPLICATION_JSON)), gameHandler::create);
  }
}
