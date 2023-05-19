package org.magcube.datastore;

import org.magcube.game.Game;
import reactor.core.publisher.Mono;

public interface GameInMemoryDataStore {
  void saveGame(String id, Game game);
  Mono<String> saveGame(Game game);
  Mono<Game> getGame(String id);
}
