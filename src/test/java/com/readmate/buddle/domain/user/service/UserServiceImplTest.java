package com.readmate.buddle.domain.user.service;

import static com.readmate.buddle.domain.user.entity.SocialProvider.GOOGLE;
import static com.readmate.buddle.domain.user.entity.SocialProvider.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

import com.readmate.buddle.domain.user.entity.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceImplTest {

  @Autowired private UserService userService;

  @Test
  @DisplayName("같은 소셜 계정으로 다시 로그인하면 같은 회원을 돌려준다")
  void returnsSameUserForSameSocialAccount() {
    UserId firstLogin = userService.findOrRegister(GOOGLE, "google-123");
    UserId secondLogin = userService.findOrRegister(GOOGLE, "google-123");

    assertThat(secondLogin).isEqualTo(firstLogin);
  }

  @Test
  @DisplayName("제공자가 다르면 식별값이 같아도 다른 회원으로 가입한다")
  void registersDifferentUserForDifferentProvider() {
    UserId googleUser = userService.findOrRegister(GOOGLE, "same-123");
    UserId kakaoUser = userService.findOrRegister(KAKAO, "same-123");

    assertThat(kakaoUser).isNotEqualTo(googleUser);
  }
}
