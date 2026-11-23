package com.readmate.buddle.global.response;

import static org.springframework.http.HttpStatus.OK;

import com.readmate.buddle.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public record ApiResponse<T>(
    int statusCode, LocalDateTime timestamp, String path, String message, T data, String error) {

  private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
  private static final String SUCCESS_MESSAGE = "요청에 성공했습니다.";

  public static <T> ApiResponse<T> success(T data) {
    return success(OK, data);
  }

  public static <T> ApiResponse<T> success(HttpStatus status, T data) {
    return new ApiResponse<>(status.value(), now(), currentPath(), SUCCESS_MESSAGE, data, null);
  }

  public static ApiResponse<Void> failure(ErrorCode errorCode) {
    HttpStatus status = errorCode.status();
    return new ApiResponse<>(
        status.value(), now(), currentPath(), errorCode.message(), null, errorCode.name());
  }

  private static LocalDateTime now() {
    return LocalDateTime.now(SEOUL);
  }

  private static String currentPath() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    return request.getRequestURI();
  }
}
