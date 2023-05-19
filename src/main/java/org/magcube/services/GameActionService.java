package org.magcube.services;

import lombok.extern.slf4j.Slf4j;
import org.magcube.database.dto.DBGame;
import org.magcube.database.service.GamePersistenceDataAccessService;
import org.magcube.datastore.GameInMemoryDataStore;
import org.magcube.datastore.GameInMemoryDataStoreProvider;
import org.magcube.enums.NumOfPlayers;
import org.magcube.exception.GameStartupException;
import org.magcube.game.GameImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GameActionService {

  private final GamePersistenceDataAccessService gamePersistenceDataAccessService;
  private final GameInMemoryDataStore gameInMemoryDataStore;

  public GameActionService(GamePersistenceDataAccessService gamePersistenceDataAccessService,
      GameInMemoryDataStoreProvider gameInMemoryDataStoreProvider) {
    this.gamePersistenceDataAccessService = gamePersistenceDataAccessService;
    this.gameInMemoryDataStore = gameInMemoryDataStoreProvider.getGameInMemoryDataStore();
  }

  public Mono<DBGame> createGame(NumOfPlayers numberOfPlayers) {
    var game = new GameImpl(numberOfPlayers);
    return gamePersistenceDataAccessService.insert(game).map(dbGame -> {
      gameInMemoryDataStore.saveGame(dbGame.getId(), game);
      return dbGame;
    });
  }

  public Mono<Boolean> startGame(String gameId) {
    return gameInMemoryDataStore.getGame(gameId).map(game -> {
      try {
        game.startGame();
      } catch (GameStartupException e) {
        log.error("start game {} failed, exception: {}", gameId, e.getMessage());
        return false;
      }
      return true;
    });
  }
}
