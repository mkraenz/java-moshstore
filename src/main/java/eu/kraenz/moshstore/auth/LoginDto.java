package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
  @NotBlank @Email @Lowercase private String email;

  @NotBlank
  @Size(min = 6, max = 64)
  private String password;
}
