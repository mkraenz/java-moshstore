package eu.kraenz.moshstore.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
class JwtService {
  @Value("${spring.jwt.secret}")
  private String signingSecret;

  public String generateToken(String email) {
    final long tokenExpiration = 86400; // 1 day

    System.out.println(signingSecret);
    return Jwts.builder()
        .subject(email)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000))
        .signWith(Keys.hmacShaKeyFor(signingSecret.getBytes()))
        .compact();
  }

  public boolean isValid(String token) {
    try {
      var payload =
          Jwts.parser()
              .verifyWith(Keys.hmacShaKeyFor(signingSecret.getBytes()))
              .build()
              .parseSignedClaims(token)
              .getPayload();
      return payload.getExpiration().after(new Date());
    } catch (JwtException e) {
      // token invalid
      return false;
    }
  }
}
