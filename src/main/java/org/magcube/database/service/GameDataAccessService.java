package org.magcube.database.service;

import lombok.extern.slf4j.Slf4j;
import org.magcube.database.dto.DBGame;
import org.magcube.database.repository.GameRepository;
import org.magcube.game.Game;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GameDataAccessService {
  private final GameRepository gameRepository;

  public GameDataAccessService(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public Mono<DBGame> findById(String id) {
    return gameRepository.findById(id);
  }

  public Mono<DBGame> insert(Game game) {
    var dbGame = DBGame.builder().game(game).build();
    return gameRepository.insert(dbGame);
  }
}
