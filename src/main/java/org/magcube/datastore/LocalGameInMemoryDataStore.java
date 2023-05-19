package org.magcube.datastore;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.magcube.game.Game;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LocalGameInMemoryDataStore implements GameInMemoryDataStore {
  private final Map<String, Game> games = new ConcurrentHashMap<>();

  @Override
  public void saveGame(String id, Game game) {
    games.put(id, game);
  }

  /**
   * Saves the game and generate new uuid for it, thread safe
   * @return the id of the game
   */
  @Override
  public synchronized Mono<String> saveGame(Game game) {
    var id = UUID.randomUUID().toString();
    games.put(id, game);
    return Mono.just(id);
  }

  @Override
  public Mono<Game> getGame(String id) {
    return Mono.just(games.get(id));
  }
}
