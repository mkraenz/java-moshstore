package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.dtos.UserDto;
import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import eu.kraenz.moshstore.mappers.UserMapper;
import eu.kraenz.moshstore.repositories.UserRepository;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
class AuthController {
  private final AuthenticationManager authManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @PostMapping("/login")
  public ResponseEntity<JwtResponseDto> logIn(@Valid @RequestBody LoginDto inputDto) {
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(inputDto.getEmail(), inputDto.getPassword()));
    var user = userRepository.findByEmail(inputDto.getEmail()).orElseThrow();
    var token = jwtService.generateToken(user.getId(), user.getEmail(), user.getName());
    return ResponseEntity.ok(new JwtResponseDto(token));
  }

  @PostMapping("/validate")
  public boolean validate(@RequestHeader("Authorization") String authHeader) {
    var token = authHeader.replace("Bearer ", "");
    return jwtService.isValid(token);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var id = (Long) authentication.getPrincipal();
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    var dto = userMapper.toDto(user);
    return ResponseEntity.ok(dto);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleInvalidCredentials() {
    return CustomHttpResponse.invalidCredentials();
  }
}
