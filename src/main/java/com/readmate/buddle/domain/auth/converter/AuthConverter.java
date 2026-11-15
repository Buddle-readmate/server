package com.readmate.buddle.domain.auth.converter;

import com.readmate.buddle.domain.auth.dto.response.LoginResponse;
import com.readmate.buddle.domain.auth.entity.AccessToken;
import com.readmate.buddle.domain.auth.entity.LoginResult;

public final class AuthConverter {

  private AuthConverter() {}

  public static LoginResponse toLoginResponse(LoginResult loginResult) {
    AccessToken accessToken = loginResult.accessToken();
    String accessTokenValue = accessToken.value();
    return new LoginResponse(accessTokenValue, loginResult.onboarded());
  }
}
