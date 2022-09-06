package com.mike.kniffel.exception.ws;

import com.mike.kniffel.common.WebSocketResponse;
import com.mike.kniffel.common.WebSocketResponseMessageType;
import java.util.UUID;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class WebSocketExceptionHandler {

  private final SimpMessagingTemplate template;

  public WebSocketExceptionHandler(final SimpMessagingTemplate template) {
    this.template = template;
  }

  @MessageExceptionHandler(IllegalWebSocketRequestException.class)
  public void handleIllegalWebSocketRequestException(final IllegalWebSocketRequestException ex) {

    final UUID playerId =
        (UUID) SimpAttributesContextHolder.currentAttributes().getAttribute("playerId");
    final String gameId =
        (String) SimpAttributesContextHolder.currentAttributes().getAttribute("gameId");

    final WebSocketResponse errorResponse =
        WebSocketResponse.builder()
            .messageType(WebSocketResponseMessageType.ERROR)
            .status(ex.getStatus())
            .build();

    this.template.convertAndSend("/topic/games/" + gameId + "/player/" + playerId, errorResponse);
  }
}
