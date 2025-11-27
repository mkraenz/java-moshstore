package eu.kraenz.moshstore.users;

import eu.kraenz.moshstore.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);

  User toEntity(CreateUserDto dto);

  void update(UpdateUserDto dto, @MappingTarget User user);
}
