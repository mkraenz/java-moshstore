package eu.kraenz.moshstore.httpErrors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public abstract class CustomHttpResponse {
  public static ResponseEntity<Map<String, String>> resourceNotFound(String resourceName) {
    var body = Map.of("message", resourceName + " not found.", "error", "Not Found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }
}
