package com.readmate.buddle.domain.user.service;

import static com.readmate.buddle.domain.user.entity.SocialProvider.GOOGLE;
import static com.readmate.buddle.domain.user.entity.SocialProvider.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.domain.user.entity.UserLoginInfo;
import com.readmate.buddle.domain.user.event.UserWithdrawnEvent;
import com.readmate.buddle.global.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@RecordApplicationEvents
class UserServiceImplTest {

  @Autowired private UserService userService;

  @Autowired private ApplicationEvents applicationEvents;

  @Test
  @DisplayName("같은 소셜 계정으로 다시 로그인하면 같은 회원을 돌려준다")
  void returnsSameUserForSameSocialAccount() {
    UserLoginInfo firstLogin = userService.findOrRegister(GOOGLE, "google-123");
    UserLoginInfo secondLogin = userService.findOrRegister(GOOGLE, "google-123");

    assertThat(secondLogin.userId()).isEqualTo(firstLogin.userId());
  }

  @Test
  @DisplayName("제공자가 다르면 식별값이 같아도 다른 회원으로 가입한다")
  void registersDifferentUserForDifferentProvider() {
    UserLoginInfo googleUser = userService.findOrRegister(GOOGLE, "same-123");
    UserLoginInfo kakaoUser = userService.findOrRegister(KAKAO, "same-123");

    assertThat(kakaoUser.userId()).isNotEqualTo(googleUser.userId());
  }

  @Test
  @DisplayName("새로 가입한 회원은 온보딩 전 상태다")
  void newUserIsNotOnboarded() {
    UserLoginInfo newUser = userService.findOrRegister(GOOGLE, "new-123");

    assertThat(newUser.onboarded()).isFalse();
  }

  @Test
  @DisplayName("탈퇴하면 회원이 삭제되고 탈퇴 이벤트가 발행된다")
  void withdrawDeletesUserAndPublishesEvent() {
    UserLoginInfo user = userService.findOrRegister(GOOGLE, "withdraw-123");

    userService.withdraw(user.userId());

    assertThat(userService.exists(user.userId())).isFalse();
    assertThat(applicationEvents.stream(UserWithdrawnEvent.class)).hasSize(1);
  }

  @Test
  @DisplayName("없는 회원은 탈퇴할 수 없다")
  void withdrawFailsForUnknownUser() {
    assertThatThrownBy(() -> userService.withdraw(new UserId(-1L)))
        .isInstanceOf(BusinessException.class);
  }
}
