package com.readmate.buddle.domain.user.service.impl;

import static com.readmate.buddle.domain.user.exception.UserErrorCode.USER_NOT_FOUND;

import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.domain.user.entity.User;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.domain.user.entity.UserLoginInfo;
import com.readmate.buddle.domain.user.event.UserWithdrawnEvent;
import com.readmate.buddle.domain.user.repository.UserRepository;
import com.readmate.buddle.domain.user.service.UserService;
import com.readmate.buddle.global.exception.BusinessException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public UserLoginInfo findOrRegister(SocialProvider provider, String providerId) {
    Optional<User> foundUser = userRepository.findByProviderAndProviderId(provider, providerId);
    User user = foundUser.orElseGet(() -> register(provider, providerId));
    return user.toLoginInfo();
  }

  private User register(SocialProvider provider, String providerId) {
    User newUser = User.register(provider, providerId);
    return userRepository.save(newUser);
  }

  @Override
  @Transactional
  public void withdraw(UserId userId) {
    User user = findUser(userId);
    eventPublisher.publishEvent(new UserWithdrawnEvent(userId));
    userRepository.delete(user);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean exists(UserId userId) {
    return userRepository.existsById(userId.value());
  }

  private User findUser(UserId userId) {
    Optional<User> user = userRepository.findById(userId.value());
    return user.orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
  }
}
