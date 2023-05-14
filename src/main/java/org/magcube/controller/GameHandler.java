package org.magcube.controller;

import lombok.extern.slf4j.Slf4j;
import org.magcube.database.service.GameDataAccessService;
import org.magcube.dto.request.GameCreateRequest;
import org.magcube.dto.request.GetGameRequest;
import org.magcube.dto.response.GameCreatedResponse;
import org.magcube.game.GameImpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GameHandler {

  private final GameDataAccessService gameDataAccessService;

  public GameHandler(GameDataAccessService gameDataAccessService) {
    this.gameDataAccessService = gameDataAccessService;
  }

  public Mono<ServerResponse> create(ServerRequest request) {
    var numOfPlayersMono = request.bodyToMono(GameCreateRequest.class);
    return numOfPlayersMono.flatMap(requestBody -> {
      var game = new GameImpl(requestBody.numOfPlayers());
      return gameDataAccessService.insert(game).flatMap(dbGame -> {
        var response = GameCreatedResponse.builder().id(dbGame.getId()).build();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response));
      });
    });
  }

  public Mono<ServerResponse> getGameBoardState(ServerRequest request) {
    var gameIdOpt = request.queryParam("gameId");
    if (gameIdOpt.isPresent()) {
      var gameId = gameIdOpt.get();
      var dbGameMono = gameDataAccessService.findById(gameId);
      return dbGameMono.flatMap(dbGame ->
              ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                  .body(BodyInserters.fromValue(dbGame.getGame().gameBoardState())))
          .switchIfEmpty(ServerResponse.notFound().build());
    } else {
      return ServerResponse.badRequest().build();
    }
  }
}
