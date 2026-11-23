package com.readmate.buddle.domain.auth.entity;

import java.time.Duration;
import java.time.Instant;

public record IssuedAccessToken(String tokenId, Instant expiresAt) {

  private static final Duration CLOCK_SKEW = Duration.ofSeconds(60);

  public Duration remainingLifetime(Instant now) {
    Duration untilExpiration = Duration.between(now, expiresAt);
    return untilExpiration.plus(CLOCK_SKEW);
  }
}
