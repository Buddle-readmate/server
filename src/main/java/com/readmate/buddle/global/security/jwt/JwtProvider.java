package com.readmate.buddle.global.security.jwt;

import static org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256;

import com.readmate.buddle.domain.auth.entity.AccessToken;
import com.readmate.buddle.domain.user.entity.UserId;
import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final JwtEncoder jwtEncoder;
  private final Duration accessTokenExpiration;

  public JwtProvider(
      JwtEncoder jwtEncoder,
      @Value("${jwt.access-token-expiration}") Duration accessTokenExpiration) {
    this.jwtEncoder = jwtEncoder;
    this.accessTokenExpiration = accessTokenExpiration;
  }

  public AccessToken issueAccessToken(UserId userId) {
    JwtClaimsSet claims = createClaims(userId);
    JwtEncoderParameters parameters = JwtEncoderParameters.from(createHeader(), claims);
    Jwt jwt = jwtEncoder.encode(parameters);
    return new AccessToken(jwt.getTokenValue());
  }

  private JwsHeader createHeader() {
    return JwsHeader.with(HS256).build();
  }

  private JwtClaimsSet createClaims(UserId userId) {
    Instant issuedAt = Instant.now();
    Instant expiresAt = issuedAt.plus(accessTokenExpiration);
    return JwtClaimsSet.builder()
        .subject(userId.asText())
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .build();
  }
}
