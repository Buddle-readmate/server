package com.readmate.buddle.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.readmate.buddle.domain.auth.entity.IssuedAccessToken;
import com.readmate.buddle.domain.auth.entity.LoginTokens;
import com.readmate.buddle.domain.auth.repository.RefreshTokenRepository;
import com.readmate.buddle.domain.user.entity.UserId;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceLogoutTest {

  @Autowired private AuthService authService;

  @Autowired private TokenIssuer tokenIssuer;

  @Autowired private AccessTokenBlacklist accessTokenBlacklist;

  @Autowired private RefreshTokenRepository refreshTokenRepository;

  @Test
  @DisplayName("로그아웃하면 access token은 블랙리스트에 오르고 refresh token은 삭제된다")
  void logoutRevokesBothTokens() {
    LoginTokens loginTokens = tokenIssuer.issue(new UserId(1L));
    String refreshTokenValue = loginTokens.refreshToken().token();
    IssuedAccessToken accessToken = createAccessToken();

    authService.logout(accessToken, refreshTokenValue);

    assertThat(accessTokenBlacklist.contains(accessToken.tokenId())).isTrue();
    assertThat(refreshTokenRepository.existsById(refreshTokenValue)).isFalse();
  }

  @Test
  @DisplayName("refresh token 쿠키가 없어도 로그아웃은 성공한다")
  void logoutSucceedsWithoutRefreshToken() {
    IssuedAccessToken accessToken = createAccessToken();

    assertThatCode(() -> authService.logout(accessToken, null)).doesNotThrowAnyException();
  }

  private IssuedAccessToken createAccessToken() {
    String tokenId = UUID.randomUUID().toString();
    return new IssuedAccessToken(tokenId, Instant.now().plusSeconds(3600));
  }
}
