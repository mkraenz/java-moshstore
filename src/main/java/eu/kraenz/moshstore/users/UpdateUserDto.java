package eu.kraenz.moshstore.users;

import lombok.Data;

@Data
public class UpdateUserDto {
  private String name;
  private String email;
}
