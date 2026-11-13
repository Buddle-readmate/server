package com.readmate.buddle.domain.auth.service;

import com.readmate.buddle.domain.auth.entity.RefreshToken;
import com.readmate.buddle.domain.auth.repository.RefreshTokenRepository;
import com.readmate.buddle.domain.user.entity.UserId;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenManager {

  private final RefreshTokenRepository refreshTokenRepository;
  private final Duration refreshTokenExpiration;

  public RefreshTokenManager(
      RefreshTokenRepository refreshTokenRepository,
      @Value("${jwt.refresh-token-expiration}") Duration refreshTokenExpiration) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.refreshTokenExpiration = refreshTokenExpiration;
  }

  public RefreshToken issue(UserId userId) {
    RefreshToken refreshToken = RefreshToken.issue(userId, refreshTokenExpiration);
    return refreshTokenRepository.save(refreshToken);
  }
}
