package org.magcube.http.controller;

import lombok.extern.slf4j.Slf4j;
import org.magcube.dto.request.GameCreateRequest;
import org.magcube.dto.request.StartGameRequest;
import org.magcube.dto.response.GameCreatedResponse;
import org.magcube.services.GameActionService;
import org.magcube.services.GameStatusService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GameHandler {

  private final GameActionService gameActionService;

  private final GameStatusService gameStatusService;

  public GameHandler(GameActionService gameActionService, GameStatusService gameStatusService) {
    this.gameActionService = gameActionService;
    this.gameStatusService = gameStatusService;
  }

  public Mono<ServerResponse> create(ServerRequest request) {
    var gameCreateRequestMono = request.bodyToMono(GameCreateRequest.class);
    return gameCreateRequestMono.flatMap(requestBody -> gameActionService.createGame(requestBody.numOfPlayers())
            .flatMap(dbGame -> {
              var response = GameCreatedResponse.builder().id(dbGame.getId()).build();
              return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                  .body(BodyInserters.fromValue(response));
            }).switchIfEmpty(Mono.empty()))
        .switchIfEmpty(ServerResponse.status(500).build());
  }

  public Mono<ServerResponse> startGame(ServerRequest request) {
    var startGameRequestMono = request.bodyToMono(StartGameRequest.class);
    return startGameRequestMono.flatMap(requestBody -> gameActionService.startGame(requestBody.gameId()).flatMap(succeed -> {
      if (succeed) {
        return ServerResponse.ok().build();
      } else {
        return ServerResponse.status(500).build();
      }
    }));
  }

  public Mono<ServerResponse> getGameBoardState(ServerRequest request) {
    var gameIdOpt = request.queryParam("gameId");
    if (gameIdOpt.isPresent()) {
      var gameId = gameIdOpt.get();
      return gameStatusService.getGameBoardStatus(gameId).flatMap(gameBoardState ->
              ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                  .body(BodyInserters.fromValue(gameBoardState)))
          .switchIfEmpty(ServerResponse.notFound().build());
    } else {
      return ServerResponse.badRequest().build();
    }
  }
}
