package org.magcube;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.magcube.dto.response.GameCreatedResponse;
import org.magcube.gameboard.GameBoardState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class) //  We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameRouterTest {

  // Spring Boot will create a `WebTestClient` for you,
  // already configure and ready to issue requests against "localhost:RANDOM_PORT"
  @Autowired
  private WebTestClient webTestClient;

  @Test
  void testCreateGameEndpoint() {
    webTestClient // Create a POST request to test an endpoint
        .post().uri("/game")
        .accept(MediaType.APPLICATION_JSON)
        .exchange() // and use the dedicated DSL to test assertions against the response
        .expectStatus().isOk()
        .expectBody(GameCreatedResponse.class);
  }

  @Test
  void testGetGameBoardStateEndpoint() {
    webTestClient // Create a POST request to test an endpoint
        .get().uri("/gameboard")
        .accept(MediaType.APPLICATION_JSON)
        .exchange() // and use the dedicated DSL to test assertions against the response
        .expectStatus().isOk()
        .expectBody(GameBoardState.class).value(Assertions::assertNotNull);
  }
}