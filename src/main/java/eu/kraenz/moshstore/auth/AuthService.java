package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.entities.User;
import eu.kraenz.moshstore.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;

  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Long currentUserId() {
    return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public User findCurrentUser() {
    return userRepository.findById(currentUserId()).orElseThrow(CurrentUserNotFound::new);
  }
}
