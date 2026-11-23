package com.readmate.buddle.domain.auth.service;

import static com.readmate.buddle.domain.auth.exception.AuthErrorCode.AUTH_INVALID_REFRESH_TOKEN;

import com.readmate.buddle.domain.auth.entity.AccessToken;
import com.readmate.buddle.domain.auth.entity.LoginTokens;
import com.readmate.buddle.domain.auth.entity.RefreshToken;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.domain.user.service.UserService;
import com.readmate.buddle.global.exception.BusinessException;
import com.readmate.buddle.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenIssuer {

  private final JwtProvider jwtProvider;
  private final RefreshTokenManager refreshTokenManager;
  private final UserService userService;

  public LoginTokens issue(UserId userId) {
    AccessToken accessToken = jwtProvider.issueAccessToken(userId);
    RefreshToken refreshToken = refreshTokenManager.issue(userId);
    return new LoginTokens(accessToken, refreshToken);
  }

  public LoginTokens reissue(String refreshTokenValue) {
    UserId userId = refreshTokenManager.consume(refreshTokenValue);
    validateUserExists(userId);
    return issue(userId);
  }

  private void validateUserExists(UserId userId) {
    if (userService.exists(userId)) {
      return;
    }
    throw new BusinessException(AUTH_INVALID_REFRESH_TOKEN);
  }
}
