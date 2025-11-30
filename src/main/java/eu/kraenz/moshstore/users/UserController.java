package eu.kraenz.moshstore.users;

import eu.kraenz.moshstore.app.ErrorResponseDto;
import eu.kraenz.moshstore.auth.AuthService;
import eu.kraenz.moshstore.common.httpErrors.CustomHttpResponse;
import eu.kraenz.moshstore.entities.Role;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Manage your user account.")
public class UserController {
  private final AuthService authService;
  private final UserService userService;

  @GetMapping
  public Iterable<UserDto> findAll(
      @RequestParam(required = false, name = "sort", defaultValue = "") String sortRaw) {
    // admin endpoint hence no further authZ
    return userService.findMany(sortRaw);
  }

  @GetMapping("/{id}")
  public UserDto findOne(@PathVariable Long id) {
    // admin endpoint hence no further authZ
    return userService.findOne(id);
  }

  @PostMapping
  public ResponseEntity<?> create(
      @Valid @RequestBody CreateUserDto data, UriComponentsBuilder uriBuilder) {
    var dto = userService.create(data);
    var uri = uriBuilder.path("/users/{id}").buildAndExpand(dto.getId()).toUri();
    return ResponseEntity.created(uri).body(dto);
  }

  @PutMapping("/{id}")
  public UserDto update(@PathVariable(name = "id") Long id, @RequestBody UpdateUserDto data) {
    if (id != authService.currentUserId()) throw new UserNotFound();
    return userService.update(id, data);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity delete(@PathVariable(name = "id") long id) {
    if (id != authService.currentUserId()) throw new UserNotFound();
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/change-password")
  public ResponseEntity<?> changePassword(
      @PathVariable(name = "id") long id, @RequestBody ChangePasswordDto data) {
    try {
      if (id != authService.currentUserId()) {
        return CustomHttpResponse.forbidden("Can only update password of own user.");
      }
      userService.changePassword(id, data);
      return ResponseEntity.accepted().build();
    } catch (PasswordMismatch e) {
      return CustomHttpResponse.forbidden("Old password does not match.");
    }
  }

  @ExceptionHandler(UserNotFound.class)
  public ResponseEntity<ErrorResponseDto> handleUserNotFound() {
    return CustomHttpResponse.resourceNotFound("user");
  }

  @ExceptionHandler(DuplicateEmail.class)
  public ResponseEntity<ErrorResponseDto> handleDuplicateEmail() {
    return CustomHttpResponse.unprocessableEntity("User by this email already exists.");
  }
}
