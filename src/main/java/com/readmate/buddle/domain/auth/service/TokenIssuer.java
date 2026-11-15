package com.readmate.buddle.domain.auth.service;

import com.readmate.buddle.domain.auth.entity.AccessToken;
import com.readmate.buddle.domain.auth.entity.LoginTokens;
import com.readmate.buddle.domain.auth.entity.RefreshToken;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenIssuer {

  private final JwtProvider jwtProvider;
  private final RefreshTokenManager refreshTokenManager;

  public LoginTokens issue(UserId userId) {
    AccessToken accessToken = jwtProvider.issueAccessToken(userId);
    RefreshToken refreshToken = refreshTokenManager.issue(userId);
    return new LoginTokens(accessToken, refreshToken);
  }
}
