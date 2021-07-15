package com.kniffel.general;

public enum StatusMessage {
    OK ("OK"),
    GAME_NOT_FOUND ("No game found with the given ID"),
    WRONG_PLAYER_DICE_ROLL ("Player that is not at turn rolled dice"),
    WRONG_PLAYER_DICE_CLICK ("Player that is not at turn clicked dice"),
    WRONG_PLAYER_CELL_CLICK ("Player that is not at turn clicked cell"),
    WRONG_COLUMN_CLICK ("Player clicked the column of another player"),
    NO_MORE_ROLLS ("RollCount is 0"),
    GAME_ENDED ("Game ended at round 14"),
    GAME_ALREADY_STARTED ("Game already started"),
    DICE_NOT_ROLLED ("Dice were locked before rolling"),
    NOT_HOST_TRIES_START ("Player that is not the host cannot start the game"),
    NOT_HOST_TRIES_RESTART ("Player that is not the host tries to restart the game"),
    CELL_NOT_EMPTY ("Cell is not empty");

    private final String message;

    StatusMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
