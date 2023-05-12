package org.magcube.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class GameCreatedResponse {

  @Getter
  @Setter
  private String createdGameId;

  @Builder
  public GameCreatedResponse(String id) {
    this.createdGameId = id;
  }

  @Override
  public String toString() {
    return "GameCreatedResponse{" +
        "message='" + createdGameId + "'}";
  }
}
