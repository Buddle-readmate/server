package com.readmate.buddle.global.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  private static final List<String> ALLOWED_METHODS =
      List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS");
  private static final List<String> ALLOWED_HEADERS = List.of("*");
  private static final String ALL_PATHS = "/**";

  @Bean
  public CorsConfigurationSource corsConfigurationSource(
      @Value("${cors.allowed-origins}") List<String> allowedOrigins) {
    CorsConfiguration configuration = createConfiguration(allowedOrigins);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(ALL_PATHS, configuration);
    return source;
  }

  private static CorsConfiguration createConfiguration(List<String> allowedOrigins) {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(allowedOrigins);
    configuration.setAllowedMethods(ALLOWED_METHODS);
    configuration.setAllowedHeaders(ALLOWED_HEADERS);
    configuration.setAllowCredentials(true);
    return configuration;
  }
}
