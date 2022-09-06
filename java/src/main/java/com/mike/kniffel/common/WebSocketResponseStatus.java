package com.mike.kniffel.common;

public enum WebSocketResponseStatus {
  OK,
  INVALID_REQUEST,
  GAME_NOT_FOUND,
  PLAYER_NOT_FOUND,
  PLAYER_IS_NOT_HOST,
  PLAYER_IS_NOT_AT_TURN,
  PLAYER_HAS_NOT_ROLLED,
  WRONG_TABLE_COLUMN,
  TABLE_CELL_NOT_EMPTY,
  NO_MORE_ROLL
}
