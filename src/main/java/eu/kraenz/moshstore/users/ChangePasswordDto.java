package eu.kraenz.moshstore.users;

import lombok.Data;

@Data
public class ChangePasswordDto {
  private String oldPassword;
  private String newPassword;
}
