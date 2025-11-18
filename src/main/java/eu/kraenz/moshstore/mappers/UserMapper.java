package eu.kraenz.moshstore.mappers;

import eu.kraenz.moshstore.dtos.CreateUserDto;
import eu.kraenz.moshstore.dtos.UserDto;
import eu.kraenz.moshstore.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);

  User toEntity(CreateUserDto dto);
}
