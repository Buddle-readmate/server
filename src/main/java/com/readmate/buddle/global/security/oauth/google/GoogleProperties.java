package com.readmate.buddle.global.security.oauth.google;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@ConfigurationProperties(prefix = "oauth.google")
public record GoogleProperties(String clientId, String clientSecret, String redirectUri) {

  private static final String GRANT_TYPE = "authorization_code";

  public MultiValueMap<String, String> toTokenRequestBody(String authorizationCode) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", GRANT_TYPE);
    body.add("code", authorizationCode);
    body.add("client_id", clientId);
    body.add("client_secret", clientSecret);
    body.add("redirect_uri", redirectUri);
    return body;
  }
}
