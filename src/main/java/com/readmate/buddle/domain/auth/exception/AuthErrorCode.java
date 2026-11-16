package com.readmate.buddle.domain.auth.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.readmate.buddle.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
  AUTH_SOCIAL_LOGIN_FAILED(UNAUTHORIZED, "소셜 로그인에 실패했습니다. 다시 시도해 주세요."),
  AUTH_UNSUPPORTED_SOCIAL_PROVIDER(BAD_REQUEST, "지원하지 않는 소셜 로그인입니다."),
  AUTH_TOKEN_REQUIRED(UNAUTHORIZED, "로그인이 필요합니다."),
  AUTH_INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않거나 만료되었습니다."),
  AUTH_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),
  AUTH_INVALID_REFRESH_TOKEN(UNAUTHORIZED, "로그인이 만료되었습니다. 다시 로그인해 주세요.");

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
