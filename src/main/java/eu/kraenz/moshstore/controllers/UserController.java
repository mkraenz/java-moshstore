package eu.kraenz.moshstore.controllers;

import eu.kraenz.moshstore.dtos.CreateUserDto;
import eu.kraenz.moshstore.dtos.UserDto;
import eu.kraenz.moshstore.entities.User;
import eu.kraenz.moshstore.mappers.UserMapper;
import eu.kraenz.moshstore.repositories.UserRepository;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
class UserController {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

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
  public ResponseEntity<UserDto> create(
      @RequestBody CreateUserDto data, UriComponentsBuilder uriBuilder) {
    var user = userMapper.toEntity(data);
    userRepository.save(user);
    var createdUserDto = userMapper.toDto(user);
    var uri = uriBuilder.path("/users/{id}").buildAndExpand(createdUserDto.getId()).toUri();
    return ResponseEntity.created(uri).body(createdUserDto);
  }
}
