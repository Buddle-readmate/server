package com.readmate.buddle.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

  String name();

  HttpStatus status();

  String message();
}
