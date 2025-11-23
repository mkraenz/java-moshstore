package eu.kraenz.moshstore.auth;

import eu.kraenz.moshstore.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
class AuthService {
  private final UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  void logIn(String email, String rawPassword) {
    var user = userRepository.findByEmail(email).orElseThrow(InvalidCredentials::new);
    boolean correctPassword = passwordEncoder.matches(rawPassword, user.getPassword());
    if (!correctPassword) {
      throw new InvalidCredentials();
    }
    // done
  }
}
