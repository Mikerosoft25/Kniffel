package com.mike.kniffel.game.control;

import com.mike.kniffel.game.entity.Dice;
import com.mike.kniffel.game.entity.PointsCalculateMode;
import org.springframework.stereotype.Component;

@Component
public class GamePointsCalculator {

  private static final int FULL_HOUSE_POINTS = 25;
  private static final int SMALL_STRAIGHT_POINTS = 30;
  private static final int LARGE_STRAIGHT_POINTS = 40;
  private static final int KNIFFEL_POINTS = 50;

  public static int calculatePoints(final Dice[] dices, final PointsCalculateMode mode) {
    return switch (mode) {
      case ACE -> calculateSingleNumber(dices, 1);
      case TWOS -> calculateSingleNumber(dices, 2);
      case THREES -> calculateSingleNumber(dices, 3);
      case FOURS -> calculateSingleNumber(dices, 4);
      case FIVES -> calculateSingleNumber(dices, 5);
      case SIXES -> calculateSingleNumber(dices, 6);
      case THREE_OF_A_KIND -> calculateAmountOfAKind(dices, 3);
      case FOUR_OF_A_KIND -> calculateAmountOfAKind(dices, 4);
      case FULL_HOUSE -> calculateFullHouse(dices);
      case SMALL_STRAIGHT -> calculateSmallStraight(dices);
      case LARGE_STRAIGHT -> calculateLargeStraight(dices);
      case KNIFFEL -> calculateKniffel(dices);
      case CHANCE -> calculateChance(dices);
    };
  }

  private static int calculateSingleNumber(final Dice[] dices, final int number) {
    int sum = 0;
    for (final Dice dice : dices) {
      sum += dice.getValue() == number ? number : 0;
    }

    return sum;
  }

  private static int calculateAmountOfAKind(final Dice[] dices, final int amountOfAKind) {
    int sum = 0;
    boolean containsThreeOfAKind = false;
    final int[] valueCounts = new int[6];

    for (final Dice dice : dices) {
      sum += dice.getValue();
      valueCounts[dice.getValue()-1] += 1;
      if (valueCounts[dice.getValue()-1] >= amountOfAKind) {
        containsThreeOfAKind = true;
      }
    }

    return containsThreeOfAKind ? sum : 0;
  }

  private static int calculateFullHouse(final Dice[] dices) {
    int twoOfAKindValue = -1;
    boolean containsTwoOfAKind = false;
    boolean containsThreeOfAKind = false;
    final int[] valueCounts = new int[6];

    for (final Dice dice : dices) {
      valueCounts[dice.getValue()-1] += 1;
      if (valueCounts[dice.getValue()-1] == 2) {
        containsTwoOfAKind = true;
        twoOfAKindValue = dice.getValue();
      } else if (valueCounts[dice.getValue()-1] == 3) {
        containsThreeOfAKind = true;
        if (twoOfAKindValue == dice.getValue()) {
          containsTwoOfAKind = false;
          twoOfAKindValue = -1;
        }
      }
    }

    return containsTwoOfAKind && containsThreeOfAKind ? FULL_HOUSE_POINTS : 0;
  }

  private static int calculateSmallStraight(final Dice[] dices) {
    final int[] valueCounts = new int[6];
    int straightLength = 0;

    for (final Dice dice : dices) {
      valueCounts[dice.getValue()-1] += 1;
    }

    for (final int valueCount : valueCounts) {
      if (valueCount != 0) {
        straightLength += 1;
      } else if (straightLength < 4){
        straightLength = 0;
      }
    }

    return straightLength >= 4 ? SMALL_STRAIGHT_POINTS : 0;
  }

  private static int calculateLargeStraight(final Dice[] dices) {
    final int[] valueCounts = new int[6];

    for (final Dice dice : dices) {
      valueCounts[dice.getValue()-1] += 1;
    }

    if (valueCounts[0] == 0 && valueCounts[5] == 0) {
      return 0;
    }

    for (int i = 0; i < valueCounts.length; i++) {
      if (valueCounts[i] == 0 && i != 0 && i != valueCounts.length-1) {
        return 0;
      }
    }

    return LARGE_STRAIGHT_POINTS;
  }

  private static int calculateKniffel(final Dice[] dices) {
    final int previousValue = dices[0].getValue();

    for (final Dice dice : dices) {
      if (dice.getValue() != previousValue) {
        return 0;
      }
    }

    return KNIFFEL_POINTS;
  }

  private static int calculateChance(final Dice[] dices) {
    int sum = 0;

    for (final Dice dice : dices) {
      sum += dice.getValue();
    }

    return sum;
  }
}
