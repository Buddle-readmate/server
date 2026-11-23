package com.readmate.buddle.domain.auth.service;

import static com.readmate.buddle.domain.auth.exception.AuthErrorCode.AUTH_INVALID_REFRESH_TOKEN;

import com.readmate.buddle.domain.auth.entity.RefreshToken;
import com.readmate.buddle.domain.auth.repository.RefreshTokenRepository;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.global.exception.BusinessException;
import java.time.Duration;
import java.util.Optional;
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

  public UserId consume(String tokenValue) {
    RefreshToken refreshToken = find(tokenValue);
    refreshTokenRepository.delete(refreshToken);
    return refreshToken.toUserId();
  }

  private RefreshToken find(String tokenValue) {
    validatePresent(tokenValue);
    Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(tokenValue);
    return refreshToken.orElseThrow(() -> new BusinessException(AUTH_INVALID_REFRESH_TOKEN));
  }

  private static void validatePresent(String tokenValue) {
    if (tokenValue == null) {
      throw new BusinessException(AUTH_INVALID_REFRESH_TOKEN);
    }
  }

  public void delete(String tokenValue) {
    if (tokenValue == null) {
      return;
    }
    refreshTokenRepository.deleteById(tokenValue);
  }
}
