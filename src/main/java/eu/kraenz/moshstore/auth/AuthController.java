package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.dtos.UserDto;
import eu.kraenz.moshstore.httpErrors.CustomHttpResponse;
import eu.kraenz.moshstore.mappers.UserMapper;
import eu.kraenz.moshstore.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
  private final JwtConfig jwtConfig;

  @PostMapping("/login")
  public ResponseEntity<JwtResponseDto> logIn(
      @Valid @RequestBody LoginDto inputDto, HttpServletResponse response) {
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(inputDto.getEmail(), inputDto.getPassword()));
    var user = userRepository.findByEmail(inputDto.getEmail()).orElseThrow();
    var token = jwtService.generateAccessToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    response.addCookie(createRefreshTokenCookie(refreshToken.toString()));

    return ResponseEntity.ok(new JwtResponseDto(token.toString()));
  }

  private Cookie createRefreshTokenCookie(String refreshToken) {
    var cookie = new Cookie("refreshToken", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/auth/refresh");
    cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
    cookie.setSecure(true);
    return cookie;
  }

  @PostMapping("/refresh")
  private ResponseEntity<JwtResponseDto> refreshAccessToken(
      @CookieValue("refreshToken") String refreshToken) {
    var jwt = jwtService.parse(refreshToken);
    if (jwt == null || !jwt.isValid()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    var user = userRepository.findById(jwt.getUserId()).orElseThrow();
    var accessToken = jwtService.generateAccessToken(user);
    return ResponseEntity.ok(new JwtResponseDto(accessToken.toString()));
  }

  @PostMapping("/logout")
  private ResponseEntity<Void> logOut(HttpServletResponse response) {
    var cookie = createRefreshTokenCookie("irrelevant");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    return ResponseEntity.ok().build();
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
