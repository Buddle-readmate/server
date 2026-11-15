package com.readmate.buddle.global.security.oauth;

import com.readmate.buddle.domain.user.entity.SocialProvider;

public interface SocialLoginClient {

  SocialProvider provider();

  String fetchProviderId(String authorizationCode);
}
