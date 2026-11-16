package com.readmate.buddle.domain.auth.service;

import com.readmate.buddle.domain.auth.entity.LoginResult;
import com.readmate.buddle.domain.auth.entity.LoginTokens;
import com.readmate.buddle.domain.user.entity.SocialProvider;

public interface AuthService {

  LoginResult login(SocialProvider provider, String authorizationCode);

  LoginTokens reissue(String refreshTokenValue);
}
