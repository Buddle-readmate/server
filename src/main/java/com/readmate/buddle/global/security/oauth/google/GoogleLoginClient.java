package com.readmate.buddle.global.security.oauth.google;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.global.security.oauth.SocialLoginClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleLoginClient implements SocialLoginClient {

  private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
  private static final String USER_INFO_URI = "https://www.googleapis.com/oauth2/v3/userinfo";
  private static final String BEARER_PREFIX = "Bearer ";

  private final RestClient restClient;
  private final GoogleProperties properties;

  public GoogleLoginClient(RestClient.Builder restClientBuilder, GoogleProperties properties) {
    this.restClient = restClientBuilder.build();
    this.properties = properties;
  }

  @Override
  public SocialProvider provider() {
    return SocialProvider.GOOGLE;
  }

  @Override
  public String fetchProviderId(String authorizationCode) {
    GoogleTokenResponse tokenResponse = requestToken(authorizationCode);
    GoogleUserResponse userResponse = requestUserInfo(tokenResponse.accessToken());
    return userResponse.sub();
  }

  private GoogleTokenResponse requestToken(String authorizationCode) {
    return restClient
        .post()
        .uri(TOKEN_URI)
        .contentType(APPLICATION_FORM_URLENCODED)
        .body(properties.toTokenRequestBody(authorizationCode))
        .retrieve()
        .body(GoogleTokenResponse.class);
  }

  private GoogleUserResponse requestUserInfo(String googleAccessToken) {
    return restClient
        .get()
        .uri(USER_INFO_URI)
        .header(AUTHORIZATION, BEARER_PREFIX + googleAccessToken)
        .retrieve()
        .body(GoogleUserResponse.class);
  }
}
