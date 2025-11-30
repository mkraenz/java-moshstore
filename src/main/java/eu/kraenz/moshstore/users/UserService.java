package eu.kraenz.moshstore.users;

import eu.kraenz.moshstore.common.httpErrors.CustomHttpResponse;
import eu.kraenz.moshstore.entities.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  UserDto findOne(Long id) {
    var user = userRepository.findById(id).orElseThrow(UserNotFound::new);
    return userMapper.toDto(user);
  }

  Iterable<UserDto> findMany(String sortRaw) {
    var sort = Set.of("name", "email").contains(sortRaw) ? sortRaw : "name";
    return userRepository.findAll(Sort.by(sort)).stream().map(userMapper::toDto).toList();
  }

  UserDto create(CreateUserDto data) {
    if (userRepository.existsByEmail(data.getEmail())) {
      throw new DuplicateEmail();
    }
    var user = userMapper.toEntity(data);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.USER);
    userRepository.save(user);
    log.info("Created user. id: {}", user.getId());
    return userMapper.toDto(user);
  }

  UserDto update(Long id, UpdateUserDto data) {
    var user = userRepository.findById(id).orElseThrow(UserNotFound::new);
    userMapper.update(data, user);
    userRepository.save(user);
    log.info("Updated user. id: {}", user.getId());
    return userMapper.toDto(user);
  }

  void delete(Long id) {
    log.info("Deleting user. id: {}", id);
    var user = userRepository.findById(id).orElseThrow(UserNotFound::new);
    userRepository.delete(user);
    log.info("Deleted user. id: {}", id);
  }

  void changePassword(long id, ChangePasswordDto data) throws PasswordMismatch {
    var user = userRepository.findById(id).orElseThrow(UserNotFound::new);
    if (!user.getPassword().equals(data.getOldPassword())) {
      throw new PasswordMismatch();
    }
    user.setPassword(data.getNewPassword());
    userRepository.save(user);
    log.info("Updated user password. id: {}", user.getId());
  }
}
