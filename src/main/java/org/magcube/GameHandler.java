package org.magcube;

import org.magcube.dto.GameCreatedResponse;
import org.magcube.enums.NumOfPlayers;
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

  public Mono<ServerResponse> create(ServerRequest request) {
    var game = new GameImpl();
    //TODO: should only touch the interface Game here and also may need to save to DB
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(new GameCreatedResponse()));
  }

  public Mono<ServerResponse> getGameBoardState(ServerRequest request) {
    //TODO:search from DB or from cache for the game instance and return the state
    Game game = new GameImpl();
    game.setPlayers(NumOfPlayers.TWO);
    //TODO
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(game.gameBoardState()));
  }
}
