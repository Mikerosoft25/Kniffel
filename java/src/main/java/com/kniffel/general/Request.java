package com.kniffel.general;

import com.kniffel.dice.Dice;
import com.kniffel.game.Game;
import com.kniffel.player.Player;
import com.kniffel.tableCell.TableCell;

public class Request {
    Game game;
    Player player;
    Dice dice;
    TableCell tableCell;
    String requesterUUID;

    public Request() {

    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public TableCell getTableCell() {
        return tableCell;
    }

    public void setTableCell(TableCell tableCell) {
        this.tableCell = tableCell;
    }

    public String getRequesterUUID() {
        return requesterUUID;
    }

    public void setRequesterUUID(String requesterUUID) {
        this.requesterUUID = requesterUUID;
    }
}
