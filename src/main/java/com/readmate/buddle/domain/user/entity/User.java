package com.readmate.buddle.domain.user.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.readmate.buddle.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
    name = "users",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_users_provider_provider_id",
            columnNames = {"provider", "provider_id"}))
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Enumerated(STRING)
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(name = "provider", nullable = false, length = 20)
  private SocialProvider provider;

  @Column(name = "provider_id", nullable = false)
  private String providerId;

  @Column(name = "nickname", length = 10)
  private String nickname;

  @Enumerated(STRING)
  @Column(name = "profile_image")
  private ProfileImage profileImage;

  private User(SocialProvider provider, String providerId) {
    this.provider = provider;
    this.providerId = providerId;
  }

  public static User register(SocialProvider provider, String providerId) {
    return new User(provider, providerId);
  }

  public UserId toUserId() {
    return new UserId(id);
  }

  public boolean isOnboarded() {
    return nickname != null;
  }
}
