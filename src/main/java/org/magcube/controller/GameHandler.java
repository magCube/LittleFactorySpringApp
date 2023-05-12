package org.magcube.controller;

import org.magcube.database.repository.GameRepository;
import org.magcube.database.service.GameDataAccessService;
import org.magcube.dto.request.GameCreateRequest;
import org.magcube.dto.response.GameCreatedResponse;
import org.magcube.enums.NumOfPlayers;
import org.magcube.exception.GameStartupException;
import org.magcube.game.Game;
import org.magcube.game.GameImpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GameHandler {

  private final GameDataAccessService gameDataAccessService;

  public GameHandler(GameDataAccessService gameDataAccessService) {
    this.gameDataAccessService = gameDataAccessService;
  }

  public Mono<ServerResponse> create(ServerRequest request) {
    var numOfPlayersMono = request.bodyToMono(GameCreateRequest.class);
    return numOfPlayersMono.flatMap(requestBody -> {
      var numOfPlayers = requestBody.getNumOfPlayers();
      var game = new GameImpl(numOfPlayers);
      return gameDataAccessService.insert(game).flatMap(dbGame -> {
        var response = GameCreatedResponse.builder().id(dbGame.getId()).build();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response));
      });
    });
  }

  public Mono<ServerResponse> getGameBoardState(ServerRequest request){
    //TODO:search from DB or from cache for the game instance and return the state
    Game game = new GameImpl();
    game.setPlayers(NumOfPlayers.TWO);
    try {
      game.startGame();
    } catch (GameStartupException e) {
      ServerResponse.notFound().build();
    }
    //TODO
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(game.gameBoardState()));
  }
}
