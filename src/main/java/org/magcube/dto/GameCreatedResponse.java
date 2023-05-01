package org.magcube.dto;

import lombok.Getter;
import lombok.Setter;

public class GameCreatedResponse {

  @Getter
  @Setter
  private String message;

  public GameCreatedResponse() {
    this.message = "game created!";
  }

  @Override
  public String toString() {
    return "GameCreatedResponse{" +
        "message='" + message + "'}";
  }
}
