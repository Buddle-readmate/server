package com.readmate.buddle.global.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.readmate.buddle.global.security.jwt.JwtAccessDeniedHandler;
import com.readmate.buddle.global.security.jwt.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationEntryPoint authenticationEntryPoint;
  private final JwtAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    disableUnusedFeatures(http);
    http.cors(withDefaults());
    http.sessionManagement(SecurityConfig::useStatelessSession);
    http.authorizeHttpRequests(SecurityConfig::authorizeRequests);
    http.oauth2ResourceServer(this::configureResourceServer);
    http.exceptionHandling(this::configureExceptionHandling);
    return http.build();
  }

  private void disableUnusedFeatures(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.formLogin(AbstractHttpConfigurer::disable);
    http.httpBasic(AbstractHttpConfigurer::disable);
  }

  private static void useStatelessSession(SessionManagementConfigurer<HttpSecurity> session) {
    session.sessionCreationPolicy(STATELESS);
  }

  private static void authorizeRequests(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
          authorize) {
    authorize
        .requestMatchers("/auth/login/**", "/swagger-ui/**", "/v3/api-docs/**")
        .permitAll()
        .anyRequest()
        .authenticated();
  }

  private void configureResourceServer(
      OAuth2ResourceServerConfigurer<HttpSecurity> resourceServer) {
    resourceServer.jwt(withDefaults());
    resourceServer.authenticationEntryPoint(authenticationEntryPoint);
    resourceServer.accessDeniedHandler(accessDeniedHandler);
  }

  private void configureExceptionHandling(
      ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling) {
    exceptionHandling.authenticationEntryPoint(authenticationEntryPoint);
    exceptionHandling.accessDeniedHandler(accessDeniedHandler);
  }
}
