package com.kniffel.game;

import com.kniffel.dice.Dice;
import com.kniffel.player.Player;

import java.util.ArrayList;

public class Game {
    private int id;
    private ArrayList<Player> players = new ArrayList<Player>();
    private Dice[] dices = new Dice[5];
    private boolean started = false;
    private boolean ended = false;
    private boolean restarted = false;
    private int playerTurn = 0;
    private int rollCount = 3;
    private int roundCount = 1;

    public Game() {
        for (int i = 0; i < 5; i++) {
            this.dices[i] = new Dice(i);
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public Dice[] getDices() {
        return dices;
    }

    public void setDices(Dice[] dices) {
        this.dices = dices;
    }

    public void rollDices() {
        for (Dice dice: dices) {
            if (!dice.isLocked()) {
                dice.roll();
            }
        }

        this.rollCount--;
    }

    public void changeDiceLock(int index) {
        this.dices[index].changeLock();
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean isRestarted() {
        return restarted;
    }

    public void setRestarted(boolean restarted) {
        this.restarted = restarted;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setNextPlayerTurn() {
        this.playerTurn = (playerTurn + 1) % getPlayerCount();
        this.rollCount = 3;

        if (this.playerTurn == 0) {
            this.roundCount++;

            if (this.roundCount == 14) {
                this.ended = true;

                for (Player player: players) {
                    int place = 1;

                    for (Player player2: players) {
                        if (player2.getTotalPoints() > player.getTotalPoints()) {
                            place++;
                        }
                    }

                    player.setPlace(place);
                }
            }
        }

        for (Dice dice: dices) {
            dice.setLocked(false);
        }
    }

    public boolean cellIsFree(int playerID, int index) {
        return players.get(playerID).getPointsAt(index) == null;
    }

    public int getRollCount() {
        return rollCount;
    }

    public void setRollCount(int rollCount) {
        this.rollCount = rollCount;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public void reset(){
        this.roundCount = 1;
        this.rollCount = 3;
        this.playerTurn = 0;
        this.started = false;
        this.ended = false;
        this.restarted = true;
        this.dices = new Dice[5];

        for (int i = 0; i < dices.length; i++) {
            dices[i] = new Dice(i);
        }

        for (Player player : players) {
            player.resetPoints();
        }
    }

    public void determinePoints(int row) {
        int[] diceValues;
        boolean[] booleanDiceValues;
        int points = 0;

        switch (row) {
            // Alle Einer addieren
            case 0:
                for (int i = 0; i < dices.length; i++) {
                    if (dices[i].getValue() == 1) {
                        points += dices[i].getValue();
                    }
                }
                break;

            // Alle Zweier addieren
            case 1:
                for (int i = 0; i < dices.length; i++) {
                    if (dices[i].getValue() == 2) {
                        points += dices[i].getValue();
                    }
                }
                break;

            // Alle Dreier addieren
            case 2:
                for (int i = 0; i < dices.length; i++) {
                    if (dices[i].getValue() == 3) {
                        points += dices[i].getValue();
                    }
                }
                break;

            // Alle Vierer addieren
            case 3:
                for (int i = 0; i < dices.length; i++) {
                    if (dices[i].getValue() == 4) {
                        points += dices[i].getValue();
                    }
                }
                break;

            // Alle Fünfer addieren
            case 4:
                for (int i = 0; i < dices.length; i++) {
                    if (dices[i].getValue() == 5) {
                        points += dices[i].getValue();
                    }
                }
                break;

            // Alle Sechser addieren
            case 5:
                for (int i = 0; i < dices.length; i++) {
                    if (dices[i].getValue() == 6) {
                        points += dices[i].getValue();
                    }
                }
                break;

            // Dreierpasch überprüfen
            case 9:
                diceValues = new int[6];
                boolean containsTripple = false;

                for (int i = 0; i < dices.length; i++) {
                    int value = dices[i].getValue() - 1;
                    diceValues[value]++;

                    if (diceValues[value] >= 3) {
                        containsTripple = true;
                    }
                }

                if (containsTripple) {
                    for (int i = 0; i < dices.length; i++) {
                        points += dices[i].getValue();
                    }
                }
                break;

            // Viererpasch überprüfen
            case 10:
                diceValues = new int[6];
                boolean containsQuadruple = false;

                for (int i = 0; i < dices.length; i++) {
                    int value = dices[i].getValue() - 1;
                    diceValues[value]++;

                    if (diceValues[value] >= 4) {
                        containsQuadruple = true;
                    }
                }

                if (containsQuadruple) {
                    for (int i = 0; i < dices.length; i++) {
                        points += dices[i].getValue();
                    }
                }
                break;

            // Full House überprüfen
            case 11:
                diceValues = new int[6];
                boolean fullHouseDouble = false;
                boolean fullHouseTripple = false;

                for (int i = 0; i < dices.length; i++) {
                    int value = dices[i].getValue() - 1;
                    diceValues[value]++;
                }

                for (int i = 0; i < diceValues.length; i++) {
                    if (diceValues[i] == 3) {
                        fullHouseTripple = true;
                    } else if (diceValues[i] == 2) {
                        fullHouseDouble = true;
                    }
                }

                if (fullHouseDouble && fullHouseTripple) {
                    points += 25;
                }
                break;

            // Kleine Straße überprüfen
            case 12:
                booleanDiceValues = new boolean[6];
                boolean smallStreet = false;

                for (int i = 0; i < dices.length; i++) {
                    booleanDiceValues[dices[i].getValue() - 1] = true;
                }

                if (booleanDiceValues[0] && booleanDiceValues[1] && booleanDiceValues[2] && booleanDiceValues[3]) {
                    smallStreet = true;
                } else if (booleanDiceValues[1] && booleanDiceValues[2] && booleanDiceValues[3] && booleanDiceValues[4]) {
                    smallStreet = true;
                } else if (booleanDiceValues[2] && booleanDiceValues[3] && booleanDiceValues[4] && booleanDiceValues[5]) {
                    smallStreet = true;
                }

                if (smallStreet) {
                    points = 30;
                }
                break;

            // Große Straße überprüfen
            case 13:
                booleanDiceValues = new boolean[6];
                boolean bigStreet = false;

                for (int i = 0; i < dices.length; i++) {
                    booleanDiceValues[dices[i].getValue() - 1] = true;
                }

                if (booleanDiceValues[0] && booleanDiceValues[1] && booleanDiceValues[2] && booleanDiceValues[3]
                        && booleanDiceValues[4]) {
                    bigStreet = true;
                } else if (booleanDiceValues[1] && booleanDiceValues[2] && booleanDiceValues[3] && booleanDiceValues[4]
                        && booleanDiceValues[5]) {
                    bigStreet = true;
                }

                if (bigStreet) {
                    points = 40;
                }
                break;

            // Kniffel überprüfen
            case 14:
                boolean sameValue = true;
                int value = dices[0].getValue();

                for (int i = 1; i < dices.length; i++) {
                    if (value != dices[i].getValue()) {
                        sameValue = false;
                    }
                }

                if (sameValue) {
                    points = 50;
                }
                break;

            // Für Chance Augenzahlen addieren
            case 15:
                for (int i = 0; i < dices.length; i++) {
                    points += dices[i].getValue();
                }
                break;

            default:
                System.err.println("## Fehler: Ungültige row!");
                break;
        }

        players.get(playerTurn).setPointAt(row, points);
        players.get(playerTurn).calculateScores();
    }
}
