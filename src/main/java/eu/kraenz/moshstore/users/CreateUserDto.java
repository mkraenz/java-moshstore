package eu.kraenz.moshstore.users;

import eu.kraenz.moshstore.common.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {
  @NotBlank(message = "Name is required.")
  @Size(message = "Name must be less than 255 characters.")
  private String name;

  @NotBlank(message = "Email is required.")
  @Email(message = "Email must be valid.")
  @Lowercase(message = "Email must be lowercase.")
  private String email;

  @NotBlank(message = "Password is required.")
  @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters long.")
  private String password;
}
