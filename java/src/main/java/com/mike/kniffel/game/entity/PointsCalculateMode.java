package com.mike.kniffel.game.entity;

import lombok.Getter;

@Getter
public enum PointsCalculateMode {
  ACE(0),
  TWOS(1),
  THREES(2),
  FOURS(3),
  FIVES(4),
  SIXES(5),
  THREE_OF_A_KIND(9),
  FOUR_OF_A_KIND(10),
  FULL_HOUSE(11),
  SMALL_STRAIGHT(12),
  LARGE_STRAIGHT(13),
  KNIFFEL(14),
  CHANCE(15);

  private final int row;

  PointsCalculateMode(final int row) {
    this.row = row;
  }

  public static PointsCalculateMode mapRowToCalculateMode(final int row) {
    for (final PointsCalculateMode mode : PointsCalculateMode.values()) {
      if (mode.row == row) {
        return mode;
      }
    }
    throw new IllegalArgumentException(String.format("Row: [%s] ist not valid", row));
  }
}
