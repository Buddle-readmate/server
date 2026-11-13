package com.readmate.buddle.global.security.cookie;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.readmate.buddle.domain.auth.entity.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class TokenCookieManager {

  private static final String COOKIE_PATH = "/";
  private static final String SAME_SITE = "None";
  private static final String EMPTY_VALUE = "";

  private final String cookieName;
  private final Duration refreshTokenExpiration;

  public TokenCookieManager(
      @Value("${jwt.refresh-cookie-name}") String cookieName,
      @Value("${jwt.refresh-token-expiration}") Duration refreshTokenExpiration) {
    this.cookieName = cookieName;
    this.refreshTokenExpiration = refreshTokenExpiration;
  }

  public void addRefreshTokenCookie(HttpServletResponse response, RefreshToken refreshToken) {
    ResponseCookie cookie = createCookie(refreshToken.token(), refreshTokenExpiration);
    response.addHeader(SET_COOKIE, cookie.toString());
  }

  public void removeRefreshTokenCookie(HttpServletResponse response) {
    ResponseCookie cookie = createCookie(EMPTY_VALUE, Duration.ZERO);
    response.addHeader(SET_COOKIE, cookie.toString());
  }

  private ResponseCookie createCookie(String value, Duration maxAge) {
    return ResponseCookie.from(cookieName, value)
        .path(COOKIE_PATH)
        .maxAge(maxAge)
        .httpOnly(true)
        .secure(true)
        .sameSite(SAME_SITE)
        .build();
  }
}
