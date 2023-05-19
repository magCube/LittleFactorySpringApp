package org.magcube.services;

import org.magcube.database.service.GamePersistenceDataAccessService;
import org.magcube.datastore.GameInMemoryDataStore;
import org.magcube.datastore.GameInMemoryDataStoreProvider;
import org.magcube.game.Game;
import org.magcube.gameboard.GameBoardState;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameStatusService {

  private final GamePersistenceDataAccessService gamePersistenceDataAccessService;
  private final GameInMemoryDataStore gameInMemoryDataStore;

  public GameStatusService(GamePersistenceDataAccessService gamePersistenceDataAccessService,
      GameInMemoryDataStoreProvider gameInMemoryDataStoreProvider) {
    this.gamePersistenceDataAccessService = gamePersistenceDataAccessService;
    this.gameInMemoryDataStore = gameInMemoryDataStoreProvider.getGameInMemoryDataStore();
  }

  public Mono<GameBoardState> getGameBoardStatus(String gameId) {
    return gameInMemoryDataStore.getGame(gameId).map(Game::gameBoardState);
  }

}
