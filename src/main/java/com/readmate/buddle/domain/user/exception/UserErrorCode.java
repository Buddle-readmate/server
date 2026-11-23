package com.readmate.buddle.domain.user.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.readmate.buddle.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
  USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 회원입니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public HttpStatus status() {
    return status;
  }

  @Override
  public String message() {
    return message;
  }
}
