package com.readmate.buddle.domain.user.repository;

import com.readmate.buddle.domain.user.entity.SocialProvider;
import com.readmate.buddle.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByProviderAndProviderId(SocialProvider provider, String providerId);
}
