package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
  private final Claims claims;
  private final SecretKey key;

  Jwt(Claims claims, SecretKey key) {
    this.claims = claims;
    this.key = key;
  }

  @Override
  public String toString() {
    return Jwts.builder().claims(claims).signWith(key).compact();
  }

  Long getUserId() {
    return Long.valueOf(claims.getSubject());
  }

  Role getRole() {
    return Role.valueOf(claims.get("role", String.class));
  }

  boolean isValid() {
    return claims.getExpiration().after(new Date());
  }
}
