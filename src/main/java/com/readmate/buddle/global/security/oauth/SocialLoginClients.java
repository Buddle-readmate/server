package com.readmate.buddle.global.security.oauth;

import static com.readmate.buddle.domain.auth.exception.AuthErrorCode.AUTH_UNSUPPORTED_SOCIAL_PROVIDER;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.global.exception.BusinessException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class SocialLoginClients {

  private final Map<SocialProvider, SocialLoginClient> clients;

  public SocialLoginClients(List<SocialLoginClient> clientList) {
    Stream<SocialLoginClient> clientStream = clientList.stream();
    this.clients = clientStream.collect(toMap(SocialLoginClient::provider, identity()));
  }

  public String fetchProviderId(SocialProvider provider, String authorizationCode) {
    SocialLoginClient client = findClient(provider);
    return client.fetchProviderId(authorizationCode);
  }

  private SocialLoginClient findClient(SocialProvider provider) {
    Optional<SocialLoginClient> client = Optional.ofNullable(clients.get(provider));
    return client.orElseThrow(() -> new BusinessException(AUTH_UNSUPPORTED_SOCIAL_PROVIDER));
  }
}
