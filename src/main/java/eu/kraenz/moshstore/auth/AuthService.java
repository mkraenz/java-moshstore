package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.entities.User;
import eu.kraenz.moshstore.users.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
  private final AuthenticationManager authManager;
  private final UserRepository userRepository;
  private final JwtService jwtService;

  public Long currentUserId() {
    return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public User findCurrentUser() {
    return userRepository.findById(currentUserId()).orElseThrow(CurrentUserNotFound::new);
  }

  JwtResponseDto refreshAccessToken(String refreshToken) {
    var jwt = jwtService.parse(refreshToken);
    if (jwt == null || !jwt.isValid()) {
      throw new InvalidJwt();
    }
    var user = userRepository.findById(jwt.getUserId()).orElseThrow();
    var accessToken = jwtService.generateAccessToken(user);
    return new JwtResponseDto(accessToken.toString());
  }

  LogInResult logIn(String email, String password) {
    authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    var user = userRepository.findByEmail(email).orElseThrow();
    var accessToken = jwtService.generateAccessToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    return new LogInResult(accessToken, refreshToken);
  }

  @AllArgsConstructor
  class LogInResult {
    Jwt accessToken;
    Jwt refreshToken;
  }
}
