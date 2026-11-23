package com.readmate.buddle.domain.auth.service;

import static com.readmate.buddle.domain.user.entity.SocialProvider.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.readmate.buddle.domain.auth.entity.RefreshToken;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.domain.user.entity.UserLoginInfo;
import com.readmate.buddle.domain.user.service.UserService;
import com.readmate.buddle.global.exception.BusinessException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class TokenIssuerTest {

  @Autowired private TokenIssuer tokenIssuer;

  @Autowired private UserService userService;

  private UserId userId;

  @BeforeEach
  void setUp() {
    UserLoginInfo user = userService.findOrRegister(GOOGLE, "token-test-user");
    userId = user.userId();
  }

  @Test
  @DisplayName("재발급하면 새 refresh token이 나온다")
  void reissueReturnsNewRefreshToken() {
    RefreshToken oldToken = tokenIssuer.issue(userId).refreshToken();

    RefreshToken newToken = tokenIssuer.reissue(oldToken.token()).refreshToken();

    assertThat(newToken.token()).isNotEqualTo(oldToken.token());
  }

  @Test
  @DisplayName("한 번 쓴 refresh token으로는 다시 재발급할 수 없다")
  void usedRefreshTokenCannotBeReused() {
    String oldToken = tokenIssuer.issue(userId).refreshToken().token();
    tokenIssuer.reissue(oldToken);

    assertThatThrownBy(() -> tokenIssuer.reissue(oldToken)).isInstanceOf(BusinessException.class);
  }

  @Test
  @DisplayName("refresh token 쿠키가 없으면 재발급할 수 없다")
  void reissueFailsWithoutToken() {
    assertThatThrownBy(() -> tokenIssuer.reissue(null)).isInstanceOf(BusinessException.class);
  }

  @Test
  @DisplayName("탈퇴한 회원의 refresh token으로는 재발급할 수 없다")
  void reissueFailsForWithdrawnUser() {
    String refreshToken = tokenIssuer.issue(userId).refreshToken().token();
    userService.withdraw(userId);

    assertThatThrownBy(() -> tokenIssuer.reissue(refreshToken))
        .isInstanceOf(BusinessException.class);
  }
}
