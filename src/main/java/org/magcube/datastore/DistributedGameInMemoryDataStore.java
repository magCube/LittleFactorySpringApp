package org.magcube.datastore;

import java.util.UUID;
import org.magcube.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DistributedGameInMemoryDataStore implements GameInMemoryDataStore {

  private ReactiveRedisTemplate<String, Object> redisTemplate;

  public DistributedGameInMemoryDataStore(ReactiveRedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public synchronized Mono<String> saveGame(Game game) {
    var id = UUID.randomUUID().toString();
    return redisTemplate.opsForValue().set(id, game).flatMap((succeed) -> {
      if (succeed) {
        return Mono.just(id);
      } else {
        return Mono.empty();
      }
    });
  }

  @Override
  public void saveGame(String id, Game game) {
    redisTemplate.opsForValue().set(id, game);
  }

  @Override
  public Mono<Game> getGame(String id) {
    return redisTemplate.opsForValue().get(id).cast(Game.class);
  }
}
