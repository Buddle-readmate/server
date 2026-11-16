package com.readmate.buddle.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

public class PublicPathBearerTokenResolver implements BearerTokenResolver {

  private static final List<String> TOKEN_FREE_PATHS = List.of("/auth/login/", "/auth/reissue");

  private final DefaultBearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();

  @Override
  public String resolve(HttpServletRequest request) {
    if (isTokenFreePath(request.getRequestURI())) {
      return null;
    }
    return defaultResolver.resolve(request);
  }

  private static boolean isTokenFreePath(String path) {
    Stream<String> tokenFreePaths = TOKEN_FREE_PATHS.stream();
    return tokenFreePaths.anyMatch(path::startsWith);
  }
}
