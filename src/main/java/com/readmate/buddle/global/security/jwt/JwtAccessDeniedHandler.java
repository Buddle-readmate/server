package com.readmate.buddle.global.security.jwt;

import static com.readmate.buddle.domain.auth.exception.AuthErrorCode.AUTH_ACCESS_DENIED;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  private final SecurityErrorResponder errorResponder;

  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
      throws IOException {
    errorResponder.respond(response, AUTH_ACCESS_DENIED);
  }
}
