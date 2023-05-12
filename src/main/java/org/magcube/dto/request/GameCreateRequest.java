package org.magcube.dto.request;

import lombok.Getter;
import org.magcube.enums.NumOfPlayers;

@Getter
public class GameCreateRequest {

  private NumOfPlayers numOfPlayers;
}
