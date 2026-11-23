package com.readmate.buddle.domain.user.service;

import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.domain.user.entity.UserId;
import com.readmate.buddle.domain.user.entity.UserLoginInfo;

public interface UserService {

  UserLoginInfo findOrRegister(SocialProvider provider, String providerId);

  void withdraw(UserId userId);

  boolean exists(UserId userId);
}
