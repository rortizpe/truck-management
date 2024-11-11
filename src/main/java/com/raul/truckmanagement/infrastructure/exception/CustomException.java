package com.raul.truckmanagement.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomException extends RuntimeException {

  private final int status;
  private final String error;
  private final String message;

}
