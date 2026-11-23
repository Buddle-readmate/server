package com.readmate.buddle.global.security.oauth.google;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "oauth.google")
public record GoogleProperties(
    @NotBlank String clientId, @NotBlank String clientSecret, @NotBlank String redirectUri) {

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
