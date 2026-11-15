package com.readmate.buddle.domain.auth.entity;

public record LoginResult(LoginTokens loginTokens, boolean onboarded) {

  public AccessToken accessToken() {
    return loginTokens.accessToken();
  }

  public RefreshToken refreshToken() {
    return loginTokens.refreshToken();
  }
}
