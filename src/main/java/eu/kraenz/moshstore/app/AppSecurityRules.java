package eu.kraenz.moshstore.app;

import eu.kraenz.moshstore.common.SecurityRules;
import eu.kraenz.moshstore.entities.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AppSecurityRules implements SecurityRules {
  @Override
  public void configure(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
          registry) {
    registry.requestMatchers("/").permitAll();
  }
}
