package com.raul.truckmanagement.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Builder
public class ErrorResponse {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
  private LocalDateTime timestamp;
  private int status;
  private String errorHttp;
  private String errorCode;
  private String message;
  private String path;
  private List<ErrorField> fieldErrors;

}
