package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.entities.Role;
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

  public String generateAccessToken(User user) {
    return generateToken(user, config.getAccessTokenExpiration());
  }

  public String generateRefreshToken(User user) {
    return generateToken(user, config.getRefreshTokenExpiration());
  }

  private String generateToken(User user, long tokenExpirationInSeconds) {
    return Jwts.builder()
        .subject(String.valueOf(user.getId()))
        .claim("email", user.getEmail())
        .claim("name", user.getName())
        .claim("role", user.getRole())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + tokenExpirationInSeconds * 1000))
        .signWith(config.getSecretKey())
        .compact();
  }

  public boolean isValid(String token) {
    try {
      var claims = parseClaims(token);
      return claims.getExpiration().after(new Date());
    } catch (JwtException e) {
      // token invalid
      return false;
    }
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
        .verifyWith(config.getSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public Long getUserId(String token) {
    return Long.valueOf(parseClaims(token).getSubject());
  }

  public Role getRole(String token) {
    return Role.valueOf(parseClaims(token).get("role", String.class));
  }
}
