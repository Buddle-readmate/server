package com.readmate.buddle.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.readmate.buddle.domain.auth.entity.RefreshToken;
import com.readmate.buddle.domain.auth.repository.RefreshTokenRepository;
import com.readmate.buddle.domain.user.entity.UserId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RefreshTokenManagerTest {

  @Autowired private RefreshTokenManager refreshTokenManager;

  @Autowired private RefreshTokenRepository refreshTokenRepository;

  @Test
  @DisplayName("발급한 Refresh Token은 Redis에 회원 ID와 함께 저장된다")
  void issuedTokenIsSavedWithUserId() {
    UserId userId = new UserId(1L);
    RefreshToken issued = refreshTokenManager.issue(userId);

    Optional<RefreshToken> found = refreshTokenRepository.findById(issued.token());

    assertThat(found).map(RefreshToken::toUserId).contains(userId);
    // refreshTokenRepository.delete(issued);
  }
}
