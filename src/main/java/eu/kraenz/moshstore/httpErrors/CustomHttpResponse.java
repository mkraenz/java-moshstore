package eu.kraenz.moshstore.httpErrors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Central place for custom http responses, particularly http errors, in order to achieve a common
 * api response format of <code>{ "message": "some message", "error": "HTTP_ERROR_IN_CAPS" }</code>
 */
public abstract class CustomHttpResponse {
  public static ResponseEntity<Map<String, String>> resourceNotFound(String resourceName) {
    var body = Map.of("message", resourceName + " not found.", "error", "NOT_FOUND");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  public static ResponseEntity<Map<String, String>> invalidCredentials() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("message", "Incorrect email or password.", "error", "UNAUTHENTICATED"));
  }
}
