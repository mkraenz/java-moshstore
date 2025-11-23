package eu.kraenz.moshstore.controllers;

import eu.kraenz.moshstore.dtos.ChangePasswordDto;
import eu.kraenz.moshstore.dtos.CreateUserDto;
import eu.kraenz.moshstore.dtos.UpdateUserDto;
import eu.kraenz.moshstore.dtos.UserDto;
import eu.kraenz.moshstore.mappers.UserMapper;
import eu.kraenz.moshstore.repositories.UserRepository;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
class UserController {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @GetMapping
  public Iterable<UserDto> findAll(
      @RequestParam(required = false, name = "sort", defaultValue = "") String sortRaw) {
    var sort = Set.of("name", "email").contains(sortRaw) ? sortRaw : "name";
    return userRepository.findAll(Sort.by(sort)).stream().map(userMapper::toDto).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> findOne(@PathVariable Long id) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @PostMapping
  public ResponseEntity<?> create(
      @Valid @RequestBody CreateUserDto data, UriComponentsBuilder uriBuilder) {
    if (userRepository.existsByEmail(data.getEmail())) {
      return ResponseEntity.unprocessableEntity()
          // questionable in terms of security: this allows user enumeration
          .body(Map.of("email", "User by this email already exists."));
    }
    var user = userMapper.toEntity(data);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    var dto = userMapper.toDto(user);
    var uri = uriBuilder.path("/users/{id}").buildAndExpand(dto.getId()).toUri();
    return ResponseEntity.created(uri).body(dto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> update(
      @PathVariable(name = "id") Long id, @RequestBody UpdateUserDto data) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    userMapper.update(data, user);
    userRepository.save(user);
    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    userRepository.delete(user);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/change-password")
  public ResponseEntity<Void> changePassword(
      @PathVariable(name = "id") long id, @RequestBody ChangePasswordDto data) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    if (!user.getPassword().equals(data.getOldPassword())) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    user.setPassword(data.getNewPassword());
    userRepository.save(user);
    return ResponseEntity.accepted().build();
  }
}
