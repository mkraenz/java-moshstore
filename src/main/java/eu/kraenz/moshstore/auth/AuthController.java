package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
class AuthController {
  private final AuthenticationManager authManager;
  private final JwtService jwtService;

  @PostMapping("/login")
  public ResponseEntity<JwtResponseDto> logIn(@Valid @RequestBody LoginDto inputDto) {
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(inputDto.getEmail(), inputDto.getPassword()));
    var token = jwtService.generateToken(inputDto.getEmail());
    return ResponseEntity.ok(new JwtResponseDto(token));
  }

  @PostMapping("/validate")
  public boolean validate(@RequestHeader("Authorization") String authHeader) {
    var token = authHeader.replace("Bearer ", "");
    return jwtService.isValid(token);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleInvalidCredentials() {
    return CustomHttpResponse.invalidCredentials();
  }
}
