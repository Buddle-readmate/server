package com.readmate.buddle.global.security.jwt;

import static com.readmate.buddle.domain.auth.exception.AuthErrorCode.AUTH_INVALID_TOKEN;
import static com.readmate.buddle.domain.auth.exception.AuthErrorCode.AUTH_TOKEN_REQUIRED;

import com.readmate.buddle.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final SecurityErrorResponder errorResponder;

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    errorResponder.respond(response, selectErrorCode(exception));
  }

  private static ErrorCode selectErrorCode(AuthenticationException exception) {
    if (exception instanceof OAuth2AuthenticationException) {
      return AUTH_INVALID_TOKEN;
    }
    return AUTH_TOKEN_REQUIRED;
  }
}
