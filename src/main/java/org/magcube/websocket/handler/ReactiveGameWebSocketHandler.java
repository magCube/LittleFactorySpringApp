package org.magcube.websocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.magcube.websocket.dto.GameUpdateDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
@Slf4j
public class ReactiveGameWebSocketHandler implements WebSocketHandler {

  //TODO: temp use a map to store mapping of gameId to session, need to find a better way
  private final Map<String, String> gameSessionMap = new HashMap<>();
  private final ObjectMapper objectMapper;
  private Sinks.Many<String> eventPublisher;
  private Flux<String> outputEvents;

  public ReactiveGameWebSocketHandler(Sinks.Many<String> eventPublisher, Flux<String> events) {
    this.eventPublisher = eventPublisher;
    this.objectMapper = new ObjectMapper();
    this.outputEvents = Flux.from(events);
  }

  @Override
  public Mono<Void> handle(WebSocketSession webSocketSession) {
    return webSocketSession
        .receive()
        .map(WebSocketMessage::getPayloadAsText)
        .log()
        .map(this::toEvent)
        .map(message -> {
          handleMessage(webSocketSession, message);
          return message;
        })
        .zipWith(webSocketSession.send(outputEvents.map(webSocketSession::textMessage)))
        .then();
  }

  private GameUpdateDto toEvent(String json) {
    try {
      return objectMapper.readValue(json, GameUpdateDto.class);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private String toJSON(GameUpdateDto event) {
    try {
      return objectMapper.writeValueAsString(event);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean handleMessage(WebSocketSession webSocketSession, GameUpdateDto message) {
    var gameId = message.gameId();
    if (!gameSessionMap.containsKey(gameId)) {
      log.info("new game session for game {}", gameId);
      gameSessionMap.put(gameId, webSocketSession.getId());
      eventPublisher.tryEmitNext("game session created, session id: " + webSocketSession.getId());
    }
    return true;
  }

}
