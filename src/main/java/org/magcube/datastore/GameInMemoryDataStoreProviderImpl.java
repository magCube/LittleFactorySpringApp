package org.magcube.datastore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GameInMemoryDataStoreProviderImpl implements GameInMemoryDataStoreProvider{
  private final GameInMemoryDataStore gameDataStore;

  public GameInMemoryDataStoreProviderImpl(@Value("${game.datastore}") String gameDataStoreType,
      LocalGameInMemoryDataStore localGameDataStore,
      DistributedGameInMemoryDataStore distributedGameDataStore) {
    switch (gameDataStoreType) {
      case "local" -> gameDataStore = localGameDataStore;
      case "redis" -> gameDataStore = distributedGameDataStore;
      default -> throw new IllegalArgumentException("Invalid game datastore type: " + gameDataStoreType);
    }
  }

  @Override
  public GameInMemoryDataStore getGameInMemoryDataStore() {
    return gameDataStore;
  }
}
