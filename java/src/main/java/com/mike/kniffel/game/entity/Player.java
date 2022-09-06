package com.mike.kniffel.game.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {
  private UUID id;
  private String name;
  private boolean host;
  private boolean connected;

  @Builder.Default
  private Integer[] points = {-1, -1, -1, -1, -1, -1, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0};

  public void resetPoints() {
    this.points =
        new Integer[] {-1, -1, -1, -1, -1, -1, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0};
  }
}
