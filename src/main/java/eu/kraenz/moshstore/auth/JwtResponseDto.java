package eu.kraenz.moshstore.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDto {
  private String accessToken;
}
