package com.mike.kniffel.game.boundary.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableCellDTO {
  @NotNull private Integer row;
  @NotNull private Integer column;
}
