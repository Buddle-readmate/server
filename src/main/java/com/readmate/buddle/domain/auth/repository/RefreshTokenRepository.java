package com.readmate.buddle.domain.auth.repository;

import com.readmate.buddle.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {}
