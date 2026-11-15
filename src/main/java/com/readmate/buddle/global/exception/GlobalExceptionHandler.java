package com.readmate.buddle.global.exception;

import com.readmate.buddle.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
    return createErrorResponse(exception.errorCode());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleInvalidInput(
      MethodArgumentNotValidException exception) {
    return createErrorResponse(CommonErrorCode.COMMON_INVALID_INPUT);
  }

  private ResponseEntity<ApiResponse<Void>> createErrorResponse(ErrorCode errorCode) {
    HttpStatus status = errorCode.status();
    ApiResponse<Void> body = ApiResponse.failure(errorCode);
    return ResponseEntity.status(status).body(body);
  }
}
