package org.magcube.dto.request;


import jakarta.validation.constraints.NotBlank;

@NotBlank(message = "Game ID is mandatory")
public record GetGameRequest(String gameId) {

}
