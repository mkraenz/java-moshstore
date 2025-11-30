package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.app.ErrorResponseDto;
import eu.kraenz.moshstore.common.httpErrors.CustomHttpResponse;
import eu.kraenz.moshstore.users.UserDto;
import eu.kraenz.moshstore.users.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
class AuthController {
  private final UserMapper userMapper;
  private final JwtConfig jwtConfig;
  private final AuthService authService;

  @PostMapping("/login")
  @Operation(
      summary = "Log In",
      description =
          "Log in and retrieve an access token for authentication via Authorization header, "
              + "and a refresh token as a cookie to renew access tokens without having to log in again. "
              + "For refreshing access tokens, see POST /auth/refresh")
  public JwtResponseDto logIn(@Valid @RequestBody LoginDto inputDto, HttpServletResponse response) {
    var tokens = authService.logIn(inputDto.getEmail(), inputDto.getPassword());
    response.addCookie(createRefreshTokenCookie(tokens.refreshToken.toString()));
    return new JwtResponseDto(tokens.accessToken.toString());
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
  @Operation(
      summary = "Refresh access token",
      description =
          "Uses the refresh token cookie to renew an access token without having to log in again.")
  public JwtResponseDto refreshAccessToken(@CookieValue("refreshToken") String refreshToken) {
    return authService.refreshAccessToken(refreshToken);
  }

  @PostMapping("/logout")
  @Operation(summary = "Log out", description = "Clears the refresh token cookie.")
  public ResponseEntity<Void> logOut(HttpServletResponse response) {
    var cookie = createRefreshTokenCookie("irrelevant");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/me")
  @Operation(
      summary = "Retrieve your user",
      description =
          "Retrieves your user account information. Use this to verify you are successfully logged in.")
  public UserDto me() {
    var user = authService.findCurrentUser();
    return userMapper.toDto(user);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidCredentials() {
    return CustomHttpResponse.invalidCredentials();
  }

  @ExceptionHandler(InvalidJwt.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidToken() {
    return CustomHttpResponse.unauthorized("Invalid token");
  }
}
