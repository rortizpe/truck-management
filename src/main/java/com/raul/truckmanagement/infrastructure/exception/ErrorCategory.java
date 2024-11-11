package com.raul.truckmanagement.infrastructure.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class ErrorCategory {

  public static final HttpStatus SUCCESSFUL_REQUEST = HttpStatus.OK;
  public static final HttpStatus INVALID_REQUEST = HttpStatus.BAD_REQUEST;
  public static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;
  public static final HttpStatus FORBIDDEN = HttpStatus.FORBIDDEN;
  public static final HttpStatus RESOURCE_NOT_FOUND = HttpStatus.NOT_FOUND;
  public static final HttpStatus CONFLICT = HttpStatus.CONFLICT;
  public static final HttpStatus PRECONDITION_FAILED = HttpStatus.PRECONDITION_FAILED;
  public static final HttpStatus TIMEOUT = HttpStatus.REQUEST_TIMEOUT;
  public static final HttpStatus NO_CONTENT = HttpStatus.NO_CONTENT;
  public static final HttpStatus LOCKED = HttpStatus.LOCKED;
  public static final HttpStatus INTERNAL_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
  public static final HttpStatus NOT_IMPLEMENTED = HttpStatus.NOT_IMPLEMENTED;
  public static final HttpStatus SERVICE_UNAVAILABLE = HttpStatus.SERVICE_UNAVAILABLE;
  public static final HttpStatus EXTERNAL_TIMEOUT = HttpStatus.GATEWAY_TIMEOUT;

  private ErrorCategory() {
  }

}
