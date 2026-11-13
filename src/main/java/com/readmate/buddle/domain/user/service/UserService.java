package com.readmate.buddle.domain.user.service;

import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.domain.user.entity.UserId;

public interface UserService {

  UserId findOrRegister(SocialProvider provider, String providerId);
}
