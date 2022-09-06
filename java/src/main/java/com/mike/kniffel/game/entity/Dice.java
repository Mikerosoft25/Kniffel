package com.mike.kniffel.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dice {
  private int value;
  private boolean locked;
  private int index;
}
