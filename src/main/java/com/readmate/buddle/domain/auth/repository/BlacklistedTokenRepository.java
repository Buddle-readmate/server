package com.readmate.buddle.domain.auth.repository;

import com.readmate.buddle.domain.auth.entity.BlacklistedToken;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistedTokenRepository extends CrudRepository<BlacklistedToken, String> {}
