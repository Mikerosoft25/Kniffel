package com.mike.kniffel.exception.rest;

import java.time.OffsetDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(KniffelException.class)
  public ResponseEntity<ErrorDTO> handleKniffelExceptionHandler(
      final KniffelException ex, final HttpServletRequest request) {
    final ErrorDTO errorDTO =
        ErrorDTO.builder()
            .timestamp(OffsetDateTime.now())
            .status(ex.getErrorType().getHttpStatus())
            .statusCode(ex.getErrorType().getHttpStatus().value())
            .message(ex.getMessage())
            .path(request.getServletPath())
            .build();

    return ResponseEntity.status(ex.getErrorType().getHttpStatus()).body(errorDTO);
  }

  @Override
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {

    final ErrorDTO errorDTO =
        ErrorDTO.builder()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.BAD_REQUEST)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .path(((ServletWebRequest) request).getRequest().getRequestURI())
            .build();

    return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
  }
}
