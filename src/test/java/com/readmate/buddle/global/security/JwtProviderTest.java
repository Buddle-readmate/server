package com.readmate.buddle.global.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.readmate.buddle.domain.auth.entity.AccessToken;
import com.readmate.buddle.domain.user.entity.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@SpringBootTest
class JwtProviderTest {

  @Autowired private JwtProvider jwtProvider;

  @Autowired private JwtDecoder jwtDecoder;

  @Test
  @DisplayName("발급한 Access Token을 해석하면 회원 ID가 들어 있다")
  void issuedTokenContainsUserId() {
    AccessToken accessToken = jwtProvider.issueAccessToken(new UserId(1L));

    Jwt jwt = jwtDecoder.decode(accessToken.value());

    assertThat(jwt.getSubject()).isEqualTo("1");
  }
}
