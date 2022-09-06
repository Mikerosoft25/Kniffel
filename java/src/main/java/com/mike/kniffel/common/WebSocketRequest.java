package com.mike.kniffel.common;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class WebSocketRequest<T> {
  private UUID requesterUUID;
  @Nullable private T payload;
}
