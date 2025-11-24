package eu.kraenz.moshstore.controllers;

import eu.kraenz.moshstore.dtos.ErrorResponseDto;
import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponseDto> handleUnreadableMessage() {
    return CustomHttpResponse.badRequest("Invalid request body.");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationErrors(
      MethodArgumentNotValidException exception) {
    Map<String, Object> errors = new HashMap<>();
    exception
        .getBindingResult()
        .getFieldErrors()
        .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
    return CustomHttpResponse.badRequest("Validation failed.", errors);
  }
}
