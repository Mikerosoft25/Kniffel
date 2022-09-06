package com.mike.kniffel.exception.rest;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class ErrorDTO {
  private OffsetDateTime timestamp;
  private HttpStatus status;
  private Integer statusCode;
  private String message;
  private String path;
}
