package com.readmate.buddle.domain.auth.service.impl;

import com.readmate.buddle.domain.auth.entity.LoginTokens;
import com.readmate.buddle.domain.auth.service.AuthService;
import com.readmate.buddle.domain.auth.service.TokenIssuer;
import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.domain.user.entity.UserId;
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

  @Override
  public LoginTokens login(SocialProvider provider, String authorizationCode) {
    String providerId = socialLoginClients.fetchProviderId(provider, authorizationCode);
    UserId userId = userService.findOrRegister(provider, providerId);
    return tokenIssuer.issue(userId);
  }
}
