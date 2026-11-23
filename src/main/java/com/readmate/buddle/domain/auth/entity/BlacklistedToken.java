package com.readmate.buddle.domain.auth.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.Duration;
import java.time.Instant;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("blacklist")
@NoArgsConstructor(access = PROTECTED)
public class BlacklistedToken {

  @Id private String tokenId;

  @TimeToLive private Long expirationSeconds;

  private BlacklistedToken(String tokenId, Long expirationSeconds) {
    this.tokenId = tokenId;
    this.expirationSeconds = expirationSeconds;
  }

  public static BlacklistedToken from(IssuedAccessToken accessToken, Instant now) {
    Duration remainingLifetime = accessToken.remainingLifetime(now);
    return new BlacklistedToken(accessToken.tokenId(), remainingLifetime.toSeconds());
  }
}
