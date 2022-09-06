package com.mike.kniffel.exception.rest;

import lombok.Getter;

@Getter
public class KniffelException extends RuntimeException {

  private final ErrorType errorType;

  public KniffelException(final ErrorType errorType) {
    super(errorType.getMessage());
    this.errorType = errorType;
  }
}
