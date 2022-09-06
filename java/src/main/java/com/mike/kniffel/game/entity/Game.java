package com.mike.kniffel.game.entity;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Game {
  private String id;
  private List<Player> players;
  private Dice[] dices;
  private GameStatus status;
  private Integer playerTurn;
  private Integer rollCount;
  private Integer round;

  public void addPlayer(final Player player) {
    this.players.add(player);
  }

  public void changeDiceLock(final int index) {
    this.dices[index].setLocked(!this.dices[index].isLocked());
  }

  public void unlockAllDice() {
    for (final Dice dice : this.dices) {
      dice.setLocked(false);
    }
  }
}
