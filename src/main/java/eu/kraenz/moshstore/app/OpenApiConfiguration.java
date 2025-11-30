package eu.kraenz.moshstore.app;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
  @Bean
  public OpenAPI customOpenAPI(@Value("${springdoc.my_api_version}") String appVersion) {
    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerScheme",
                    new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer"))
                .addHeaders(
                    "Authorization",
                    new Header().description("Authorization header").schema(new StringSchema())))
        .info(
            new Info()
                .title("Miros Java Store API")
                .version(appVersion)
                .description("It was fun building this.")
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")));
  }
}
