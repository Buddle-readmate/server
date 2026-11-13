package com.readmate.buddle.domain.user.entity;

public record UserId(Long value) {

  public String asText() {
    return String.valueOf(value);
  }
}
