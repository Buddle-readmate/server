package com.readmate.buddle.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SocialLoginRequest(@NotBlank String code) {}
