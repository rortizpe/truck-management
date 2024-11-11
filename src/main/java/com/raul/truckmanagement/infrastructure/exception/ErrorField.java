package com.raul.truckmanagement.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorField {
  private String name;
  private String error;
}