package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<Void> logIn(@Valid @RequestBody LoginDto inputDto) {
    authService.logIn(inputDto.getEmail(), inputDto.getPassword());
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(InvalidCredentials.class)
  public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentials e) {
    return CustomHttpResponse.invalidCredentials();
  }
}
