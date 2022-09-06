package com.mike.kniffel.game.boundary.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlayerNameDTO {
  @NotBlank(message = "playerName cannot be empty")
  private String playerName;
}
