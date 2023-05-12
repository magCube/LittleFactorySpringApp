package org.magcube.database.dto;

import lombok.Builder;
import lombok.Data;
import org.magcube.game.Game;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "game")
@Data
@Builder
public class DBGame {
  @Id
  private String id;
  private Game game;
}
