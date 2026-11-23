package com.readmate.buddle.domain.auth.entity;

import static lombok.AccessLevel.PROTECTED;

import com.readmate.buddle.domain.user.entity.UserId;
import java.time.Duration;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("refreshToken")
@NoArgsConstructor(access = PROTECTED)
public class RefreshToken {

  @Id private String token;

  private Long userId;

  @TimeToLive private Long expirationSeconds;

  private RefreshToken(String token, Long userId, Long expirationSeconds) {
    this.token = token;
    this.userId = userId;
    this.expirationSeconds = expirationSeconds;
  }

  public static RefreshToken issue(UserId userId, Duration expiration) {
    String token = UUID.randomUUID().toString();
    return new RefreshToken(token, userId.value(), expiration.toSeconds());
  }

  public UserId toUserId() {
    return new UserId(userId);
  }

  public String token() {
    return token;
  }
}
