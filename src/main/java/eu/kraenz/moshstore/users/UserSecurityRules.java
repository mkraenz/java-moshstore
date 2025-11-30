package eu.kraenz.moshstore.users;

import eu.kraenz.moshstore.common.SecurityRules;
import eu.kraenz.moshstore.entities.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class UserSecurityRules implements SecurityRules {
  @Override
  public void configure(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
          registry) {
    registry
        .requestMatchers(HttpMethod.POST, "/users")
        .permitAll()
        .requestMatchers(HttpMethod.PUT, "/users/*")
        .authenticated()
        .requestMatchers(HttpMethod.POST, "/users/*/change-password")
        .authenticated()
        .requestMatchers("/users/**")
        .hasRole(Role.ADMIN.name());
  }
}
