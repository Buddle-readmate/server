package com.readmate.buddle.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
  COMMON_INVALID_INPUT(BAD_REQUEST, "입력값이 올바르지 않습니다.");

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
