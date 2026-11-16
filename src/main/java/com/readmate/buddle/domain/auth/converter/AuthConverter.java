package com.readmate.buddle.domain.auth.converter;

import com.readmate.buddle.domain.auth.dto.response.LoginResponse;
import com.readmate.buddle.domain.auth.dto.response.ReissueResponse;
import com.readmate.buddle.domain.auth.entity.AccessToken;
import com.readmate.buddle.domain.auth.entity.LoginResult;
import com.readmate.buddle.domain.auth.entity.LoginTokens;

public final class AuthConverter {

  private AuthConverter() {}

  public static LoginResponse toLoginResponse(LoginResult loginResult) {
    AccessToken accessToken = loginResult.accessToken();
    String accessTokenValue = accessToken.value();
    return new LoginResponse(accessTokenValue, loginResult.onboarded());
  }

  public static ReissueResponse toReissueResponse(LoginTokens loginTokens) {
    AccessToken accessToken = loginTokens.accessToken();
    return new ReissueResponse(accessToken.value());
  }
}
