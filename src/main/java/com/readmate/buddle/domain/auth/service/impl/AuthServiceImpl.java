package com.readmate.buddle.domain.auth.service.impl;

import com.readmate.buddle.domain.auth.entity.IssuedAccessToken;
import com.readmate.buddle.domain.auth.entity.LoginResult;
import com.readmate.buddle.domain.auth.entity.LoginTokens;
import com.readmate.buddle.domain.auth.service.AccessTokenBlacklist;
import com.readmate.buddle.domain.auth.service.AuthService;
import com.readmate.buddle.domain.auth.service.RefreshTokenManager;
import com.readmate.buddle.domain.auth.service.TokenIssuer;
import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.domain.user.entity.UserLoginInfo;
import com.readmate.buddle.domain.user.service.UserService;
import com.readmate.buddle.global.security.oauth.SocialLoginClients;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final SocialLoginClients socialLoginClients;
  private final UserService userService;
  private final TokenIssuer tokenIssuer;
  private final AccessTokenBlacklist accessTokenBlacklist;
  private final RefreshTokenManager refreshTokenManager;

  @Override
  public LoginResult login(SocialProvider provider, String authorizationCode) {
    String providerId = socialLoginClients.fetchProviderId(provider, authorizationCode);
    UserLoginInfo userLoginInfo = userService.findOrRegister(provider, providerId);
    UserId userId = userLoginInfo.userId();
    LoginTokens loginTokens = tokenIssuer.issue(userId);
    return new LoginResult(loginTokens, userLoginInfo.onboarded());
  }

  @Override
  public LoginTokens reissue(String refreshTokenValue) {
    return tokenIssuer.reissue(refreshTokenValue);
  }

  @Override
  public void logout(IssuedAccessToken accessToken, String refreshTokenValue) {
    accessTokenBlacklist.add(accessToken);
    refreshTokenManager.delete(refreshTokenValue);
  }
}
