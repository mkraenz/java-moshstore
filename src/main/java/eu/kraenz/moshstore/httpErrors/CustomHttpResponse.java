package eu.kraenz.moshstore.httpErrors;

import eu.kraenz.moshstore.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Central place for custom http responses, particularly http errors, in order to achieve a common
 * api response format,
 */
public abstract class CustomHttpResponse {
  public static ResponseEntity<ErrorResponseDto> resourceNotFound(String resourceName) {
    return toDto(HttpStatus.NOT_FOUND, StringUtils.capitalize(resourceName) + " not found.");
  }

  public static ResponseEntity<ErrorResponseDto> invalidCredentials() {
    var error = HttpStatus.NOT_FOUND;
    var body =
        ErrorResponseDto.builder()
            .message("Incorrect email or password.")
            .error("UNAUTHENTICATED")
            .statusCode(error.value())
            .build();
    return ResponseEntity.status(error).body(body);
  }

  public static ResponseEntity<ErrorResponseDto> badRequest(String message) {
    return toDto(HttpStatus.BAD_REQUEST, message);
  }

  public static ResponseEntity<ErrorResponseDto> badRequest(
      String message, Map<String, Object> details) {
    return toDto(HttpStatus.BAD_REQUEST, message, details);
  }

  private static ResponseEntity<ErrorResponseDto> toDto(HttpStatus error, String message) {
    return toDto(error, message, null);
  }

  private static ResponseEntity<ErrorResponseDto> toDto(
      HttpStatus error, String message, Map<String, Object> details) {
    var body =
        ErrorResponseDto.builder()
            .message(message)
            .error(error.name())
            .statusCode(error.value())
            .details(details)
            .build();
    return ResponseEntity.status(error).body(body);
  }
}
