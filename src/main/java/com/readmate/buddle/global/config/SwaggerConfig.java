package com.readmate.buddle.global.config;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

import com.readmate.buddle.global.security.resolver.LoginUser;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  private static final String BEARER_SCHEME = "bearerAuth";

  static {
    SpringDocUtils config = SpringDocUtils.getConfig();
    config.addAnnotationsToIgnore(LoginUser.class);
  }

  @Bean
  public OpenAPI openApi() {
    Components components = new Components().addSecuritySchemes(BEARER_SCHEME, bearerScheme());
    SecurityRequirement requirement = new SecurityRequirement().addList(BEARER_SCHEME);
    return new OpenAPI().components(components).addSecurityItem(requirement);
  }

  private static SecurityScheme bearerScheme() {
    return new SecurityScheme().type(HTTP).scheme("bearer").bearerFormat("JWT");
  }
}
