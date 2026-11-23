package com.readmate.buddle.domain.auth.converter;

import com.readmate.buddle.domain.auth.dto.response.LoginResponse;
import com.readmate.buddle.domain.auth.dto.response.ReissueResponse;
import com.readmate.buddle.domain.auth.entity.AccessToken;
import com.readmate.buddle.domain.auth.entity.IssuedAccessToken;
import com.readmate.buddle.domain.auth.entity.LoginResult;
import com.readmate.buddle.domain.auth.entity.LoginTokens;
import java.time.Instant;
import org.springframework.security.oauth2.jwt.Jwt;

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

  public static IssuedAccessToken toIssuedAccessToken(Jwt jwt) {
    String tokenId = jwt.getId();
    Instant expiresAt = jwt.getExpiresAt();
    return new IssuedAccessToken(tokenId, expiresAt);
  }
}
