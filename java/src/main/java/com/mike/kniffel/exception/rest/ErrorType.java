package com.mike.kniffel.exception.rest;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
  GAME_IS_FULL(HttpStatus.BAD_REQUEST, "Game is already full"),
  GAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "Game could not be found"),
  Game_ALREADY_STARTED(HttpStatus.BAD_REQUEST, "Game already started"),
  PLAYER_NAME_ALREADY_EXISTS(
      HttpStatus.BAD_REQUEST, "A player with the same name already exists in this game");

  private final HttpStatus httpStatus;
  private final String message;

  private ErrorType(final HttpStatus httpStatus, final String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}
