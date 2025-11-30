package eu.kraenz.moshstore.app;

import eu.kraenz.moshstore.common.httpErrors.CustomHttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponseDto> handleUnreadableMessage() {
    return CustomHttpResponse.badRequest("Invalid request body. Not a valid JSON.");
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

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationErrors(
      MethodArgumentTypeMismatchException exception) {
    return CustomHttpResponse.badRequest(
        "Invalid path parameter.",
        Map.of("parameterName", exception.getParameter().getParameter().getName().toString()));
  }
}
