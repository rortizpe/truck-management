package com.raul.truckmanagement.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import jakarta.validation.ConstraintViolationException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class CustomControllerAdvice {

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleGenericException(
    Exception ex, ServerWebExchange exchange) {

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
      .errorHttp(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
      .message(ex.getMessage() != null ? ex.getMessage() : "Ocurrió un error inesperado.")
      .path(exchange.getRequest().getURI().getPath())
      .build();
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
  }
  @ExceptionHandler(CustomException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleCustomException(CustomException ex,
    ServerWebExchange exchange) {
    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(ex.getStatus())
      .errorHttp(ex.getError())
      .message(ex.getMessage())
      .path(exchange.getRequest().getURI().getPath())
      .build();
    return Mono.just(ResponseEntity.status(ex.getStatus()).body(errorResponse));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex, ServerWebExchange exchange) {
    List<ErrorField> listError = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(e -> new ErrorField(e.getField(), e.getDefaultMessage()))
      .collect(Collectors.toList());

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .errorHttp(HttpStatus.BAD_REQUEST.getReasonPhrase())
      .message("Error de validación")
      .fieldErrors(listError)
      .path(exchange.getRequest().getURI().getPath())
      .build();
    return Mono.just(ResponseEntity.badRequest().body(errorResponse));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleConstraintViolation(
    ConstraintViolationException ex, ServerWebExchange exchange) {
    List<ErrorField> listError = ex.getConstraintViolations()
      .stream()
      .map(violation -> new ErrorField("Valor inválido: " + violation.getInvalidValue(),
        violation.getMessage()))
      .collect(Collectors.toList());

    ErrorResponse errorResponse = ErrorResponse.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.BAD_REQUEST.value())
      .errorHttp(HttpStatus.BAD_REQUEST.getReasonPhrase())
      .message("Error de validación")
      .fieldErrors(listError)
      .path(exchange.getRequest().getURI().getPath())
      .build();
    return Mono.just(ResponseEntity.badRequest().body(errorResponse));
  }
}