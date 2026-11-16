package com.readmate.buddle.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.readmate.buddle.domain.auth.entity.LoginTokens;
import com.readmate.buddle.domain.auth.entity.RefreshToken;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.global.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenIssuerTest {

  @Autowired private TokenIssuer tokenIssuer;

  @Test
  @DisplayName("재발급하면 새 refresh token이 나온다")
  void reissueReturnsNewRefreshToken() {
    RefreshToken oldToken = tokenIssuer.issue(new UserId(1L)).refreshToken();

    RefreshToken newToken = tokenIssuer.reissue(oldToken.token()).refreshToken();

    assertThat(newToken.token()).isNotEqualTo(oldToken.token());
  }

  @Test
  @DisplayName("한 번 쓴 refresh token으로는 다시 재발급할 수 없다")
  void usedRefreshTokenCannotBeReused() {
    LoginTokens loginTokens = tokenIssuer.issue(new UserId(1L));
    String oldToken = loginTokens.refreshToken().token();
    tokenIssuer.reissue(oldToken);

    assertThatThrownBy(() -> tokenIssuer.reissue(oldToken)).isInstanceOf(BusinessException.class);
  }

  @Test
  @DisplayName("refresh token 쿠키가 없으면 재발급할 수 없다")
  void reissueFailsWithoutToken() {
    assertThatThrownBy(() -> tokenIssuer.reissue(null)).isInstanceOf(BusinessException.class);
  }
}
