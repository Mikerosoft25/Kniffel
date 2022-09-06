package com.mike.kniffel.exception.ws;

import com.mike.kniffel.common.WebSocketResponseStatus;
import lombok.Getter;

@Getter
public class IllegalWebSocketRequestException extends RuntimeException {

  private final WebSocketResponseStatus status;

  public IllegalWebSocketRequestException(final WebSocketResponseStatus status) {
    super();
    this.status = status;
  }
}
