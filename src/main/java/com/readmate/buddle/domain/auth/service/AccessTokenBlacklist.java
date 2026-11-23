package com.readmate.buddle.domain.auth.service;

import com.readmate.buddle.domain.auth.entity.BlacklistedToken;
import com.readmate.buddle.domain.auth.entity.IssuedAccessToken;
import com.readmate.buddle.domain.auth.repository.BlacklistedTokenRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenBlacklist {

  private final BlacklistedTokenRepository blacklistedTokenRepository;

  public void add(IssuedAccessToken accessToken) {
    BlacklistedToken blacklistedToken = BlacklistedToken.from(accessToken, Instant.now());
    blacklistedTokenRepository.save(blacklistedToken);
  }

  public boolean contains(String tokenId) {
    if (tokenId == null) {
      return false;
    }
    return blacklistedTokenRepository.existsById(tokenId);
  }
}
