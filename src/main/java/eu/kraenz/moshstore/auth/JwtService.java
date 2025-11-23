package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class JwtService {
  private final JwtConfig config;

  public Jwt generateAccessToken(User user) {
    return generateToken(user, config.getAccessTokenExpiration());
  }

  public Jwt generateRefreshToken(User user) {
    return generateToken(user, config.getRefreshTokenExpiration());
  }

  private Jwt generateToken(User user, long tokenExpirationInSeconds) {
    var claims =
        Jwts.claims()
            .subject(String.valueOf(user.getId()))
            .add("email", user.getEmail())
            .add("name", user.getName())
            .add("role", user.getRole())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + tokenExpirationInSeconds * 1000))
            .build();
    return new Jwt(claims, config.getSecretKey());
  }

  public Jwt parse(String token) {
    try {
      var claims = parseClaims(token);
      return new Jwt(claims, config.getSecretKey());
    } catch (JwtException e) {
      return null;
    }
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(config.getSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
