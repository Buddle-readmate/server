package com.readmate.buddle.domain.auth.service;

import com.readmate.buddle.domain.auth.entity.IssuedAccessToken;
import com.readmate.buddle.domain.auth.entity.LoginResult;
import com.readmate.buddle.domain.auth.entity.LoginTokens;
import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.domain.user.entity.UserId;

public interface AuthService {

  LoginResult login(SocialProvider provider, String authorizationCode);

  LoginTokens reissue(String refreshTokenValue);

  void logout(IssuedAccessToken accessToken, String refreshTokenValue);

  void withdraw(UserId userId, IssuedAccessToken accessToken, String refreshTokenValue);
}
