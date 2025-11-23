package eu.kraenz.moshstore.auth;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfig {
  private String secret;
  private int accessTokenExpiration;
  private int refreshTokenExpiration;

  public SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }
}
