package com.readmate.buddle.domain.user.service.impl;

import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.domain.user.entity.User;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.domain.user.repository.UserRepository;
import com.readmate.buddle.domain.user.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserId findOrRegister(SocialProvider provider, String providerId) {
    Optional<User> foundUser = userRepository.findByProviderAndProviderId(provider, providerId);
    User user = foundUser.orElseGet(() -> register(provider, providerId));
    return user.toUserId();
  }

  private User register(SocialProvider provider, String providerId) {
    User newUser = User.register(provider, providerId);
    return userRepository.save(newUser);
  }
}
