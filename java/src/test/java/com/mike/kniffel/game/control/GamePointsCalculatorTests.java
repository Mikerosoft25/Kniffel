package com.mike.kniffel.game.control;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mike.kniffel.game.entity.Dice;
import com.mike.kniffel.game.entity.PointsCalculateMode;
import org.junit.jupiter.api.Test;

class GamePointsCalculatorTests {

  //  Aces
  @Test
  void calculatePointsAces_withNoAce_shouldReturn0() {
    final Dice[] dices = getDices(6, 2, 3, 4, 5);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.ACE);

    assertEquals(0, points);
  }

  @Test
  void calculatePointsAces_with1Ace_shouldReturn1() {
    final Dice[] dices = getDices(1, 2, 3, 4, 5);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.ACE);

    assertEquals(1, points);
  }

  @Test
  void calculatePointsAces_with5Aces_shouldReturn5() {
    final Dice[] dices = getDices(1, 1, 1, 1, 1);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.ACE);

    assertEquals(5, points);
  }

  //  Twos
  @Test
  void calculatePointsAces_withNoTwo_shouldReturn0() {
    final Dice[] dices = getDices(6, 5, 3, 4, 5);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.ACE);

    assertEquals(0, points);
  }

  @Test
  void calculatePointsTwos_with1Two_shouldReturn2() {
    final Dice[] dices = getDices(1, 2, 3, 4, 5);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.TWOS);

    assertEquals(2, points);
  }

  @Test
  void calculatePointsTwos_with5Twos_shouldReturn10() {
    final Dice[] dices = getDices(2, 2, 2, 2, 2);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.TWOS);

    assertEquals(10, points);
  }

  // Threes
  @Test
  void calculatePointsThrees_with5Threes_shouldReturn15() {
    final Dice[] dices = getDices(3, 3, 3, 3, 3);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.THREES);

    assertEquals(15, points);
  }

  // Fours
  @Test
  void calculatePointsFours_with5Fours_shouldReturn20() {
    final Dice[] dices = getDices(4, 4, 4, 4, 4);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.FOURS);

    assertEquals(20, points);
  }

  // Fives
  @Test
  void calculatePointsFives_with5Fives_shouldReturn25() {
    final Dice[] dices = getDices(5, 5, 5, 5, 5);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.FIVES);

    assertEquals(25, points);
  }

  // Sixes
  @Test
  void calculatePointsSixes_with5Sixes_shouldReturn30() {
    final Dice[] dices = getDices(6, 6, 6, 6, 6);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.SIXES);

    assertEquals(30, points);
  }

  // Three of a kind
  @Test
  void calculatePointsThreeOfAKind_withNone_shouldReturn0() {
    final Dice[] dices = getDices(1, 2, 4, 3, 2);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.THREE_OF_A_KIND);

    assertEquals(0, points);
  }

  @Test
  void calculatePointsThreeOfAKind_withThreeeOfAKind_shouldReturn20() {
    final Dice[] dices = getDices(5, 2, 5, 3, 5);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.THREE_OF_A_KIND);

    assertEquals(20, points);
  }

  @Test
  void calculatePointsThreeOfAKind_withKniffel_shouldReturn25() {
    final Dice[] dices = getDices(5, 5, 5, 5, 5);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.THREE_OF_A_KIND);

    assertEquals(25, points);
  }

  // Four of a kind
  @Test
  void calculatePointsFourOfAKind_withNone_shouldReturn0() {
    final Dice[] dices = getDices(1, 2, 4, 3, 2);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.FOUR_OF_A_KIND);

    assertEquals(0, points);
  }

  @Test
  void calculatePointsFourOfAKind_withFourOfAKind_shouldReturn26() {
    final Dice[] dices = getDices(5, 5, 5, 6, 5);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.FOUR_OF_A_KIND);

    assertEquals(26, points);
  }

  @Test
  void calculatePointsFourOfAKind_withKniffel_shouldReturn25() {
    final Dice[] dices = getDices(5, 5, 5, 5, 5);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.FOUR_OF_A_KIND);

    assertEquals(25, points);
  }

  // Full House
  @Test
  void calculatePointsFullHouse_withNone_shouldReturn0() {
    final Dice[] dices = getDices(5, 5, 5, 5, 5);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.FULL_HOUSE);

    assertEquals(0, points);
  }

  @Test
  void calculatePointsFullHouse_withFullHouse_shouldReturn25() {
    final Dice[] dices = getDices(5, 3, 5, 5, 3);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.FULL_HOUSE);

    assertEquals(25, points);
  }

  @Test
  void calculatePointsFullHouse_withFullHouseInOrder_shouldReturn25() {
    final Dice[] dices = getDices(5, 5, 5, 3, 3);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.FULL_HOUSE);

    assertEquals(25, points);
  }

  // Small Straight
  @Test
  void calculatePointsSmallStraight_withNone_shouldReturn0() {
    final Dice[] dices = getDices(1, 2, 4, 5, 6);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.SMALL_STRAIGHT);

    assertEquals(0, points);
  }

  @Test
  void calculatePointsSmallStraight_withSmallStraight_shouldReturn30() {
    final Dice[] dices = getDices(1, 3, 2, 4, 3);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.SMALL_STRAIGHT);

    assertEquals(30, points);
  }

  @Test
  void calculatePointsSmallStraight_withLargeStraight_shouldReturn30() {
    final Dice[] dices = getDices(1, 3, 2, 4, 5);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.SMALL_STRAIGHT);

    assertEquals(30, points);
  }

  // Large Straight
  @Test
  void calculatePointsLargeStraight_withNone_shouldReturn0() {
    final Dice[] dices = getDices(1, 2, 4, 5, 6);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.LARGE_STRAIGHT);

    assertEquals(0, points);
  }

  // Large Straight
  @Test
  void calculatePointsLargeStraight_withNoOneAnd6_shouldReturn0() {
    final Dice[] dices = getDices(2, 2, 3, 4, 5);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.LARGE_STRAIGHT);

    assertEquals(0, points);
  }

  @Test
  void calculatePointsLargeStraight_withLargeStraight_shouldReturn40() {
    final Dice[] dices = getDices(1, 3, 2, 4, 5);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.LARGE_STRAIGHT);

    assertEquals(40, points);
  }

  @Test
  void calculatePointsLargeStraight_withLargeStraightInOrder_shouldReturn40() {
    final Dice[] dices = getDices(2, 3, 4, 5, 6);

    final int points =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.LARGE_STRAIGHT);

    assertEquals(40, points);
  }

  // Kniffel
  @Test
  void calculatePointsKniffel_withNone_shouldReturn0() {
    final Dice[] dices = getDices(6, 6, 6, 5, 6);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.KNIFFEL);

    assertEquals(0, points);
  }

  @Test
  void calculatePointsKniffel_withKniffel_shouldReturn50() {
    final Dice[] dices = getDices(6, 6, 6, 6, 6);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.KNIFFEL);

    assertEquals(50, points);
  }

  // Chance
  @Test
  void calculatePointsChance_withValues_shouldReturn17() {
    final Dice[] dices = getDices(2, 4, 6, 2, 3);

    final int points = GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.CHANCE);

    assertEquals(17, points);
  }

  private Dice[] getDices(
      final int first, final int second, final int third, final int fourth, final int fifth) {
    return new Dice[] {
      new Dice(first, false, 0),
      new Dice(second, false, 1),
      new Dice(third, false, 2),
      new Dice(fourth, false, 3),
      new Dice(fifth, false, 4),
    };
  }
}
