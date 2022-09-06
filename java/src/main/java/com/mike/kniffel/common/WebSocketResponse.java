package com.mike.kniffel.common;

import com.mike.kniffel.game.entity.Game;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Builder
@Data
public class WebSocketResponse {
  @NotNull private WebSocketResponseMessageType messageType;
  @NotNull private WebSocketResponseStatus status;
  @Nullable private Game game;
}
