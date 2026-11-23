package com.readmate.buddle.global.config;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.readmate.buddle.global.security.jwt.BlacklistTokenValidator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

  private static final String HMAC_ALGORITHM = "HmacSHA256";

  private final SecretKey secretKey;

  public JwtConfig(@Value("${jwt.secret-key}") String secretKey) {
    this.secretKey = createSecretKey(secretKey);
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    JWKSource<SecurityContext> secret = new ImmutableSecret<>(secretKey);
    return new NimbusJwtEncoder(secret);
  }

  @Bean
  public JwtDecoder jwtDecoder(BlacklistTokenValidator blacklistTokenValidator) {
    NimbusJwtDecoder decoder =
        NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(HS256).build();
    decoder.setJwtValidator(createValidator(blacklistTokenValidator));
    return decoder;
  }

  private static SecretKey createSecretKey(String secretKey) {
    byte[] keyBytes = secretKey.getBytes(UTF_8);
    return new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
  }

  private static OAuth2TokenValidator<Jwt> createValidator(
      BlacklistTokenValidator blacklistTokenValidator) {
    OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefault();
    return new DelegatingOAuth2TokenValidator<>(defaultValidator, blacklistTokenValidator);
  }
}
