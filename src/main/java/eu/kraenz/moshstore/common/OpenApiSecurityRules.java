package eu.kraenz.moshstore.common;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class OpenApiSecurityRules implements SecurityRules {
  @Override
  public void configure(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
          registry) {
    registry
        .requestMatchers(HttpMethod.GET, "/docs", "/swagger-ui/*", "/v3/api-docs/**")
        .permitAll();
  }
}
