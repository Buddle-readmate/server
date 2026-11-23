package com.readmate.buddle.global.security.jwt;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_TOKEN;

import com.readmate.buddle.domain.auth.service.AccessTokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlacklistTokenValidator implements OAuth2TokenValidator<Jwt> {

  private static final OAuth2Error LOGGED_OUT_TOKEN =
      new OAuth2Error(INVALID_TOKEN, "로그아웃된 토큰입니다.", null);

  private final AccessTokenBlacklist accessTokenBlacklist;

  @Override
  public OAuth2TokenValidatorResult validate(Jwt jwt) {
    String tokenId = jwt.getId();
    if (accessTokenBlacklist.contains(tokenId)) {
      return OAuth2TokenValidatorResult.failure(LOGGED_OUT_TOKEN);
    }
    return OAuth2TokenValidatorResult.success();
  }
}
