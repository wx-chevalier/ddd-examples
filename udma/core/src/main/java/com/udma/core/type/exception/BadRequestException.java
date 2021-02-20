package com.udma.core.type.exception;

import com.udma.core.type.code.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseBizException {

  public BadRequestException() {
    super(HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(String message, ApiErrorCode code) {
    super(message, HttpStatus.BAD_REQUEST);
    this.setCode(code);
  }

  public BadRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause, HttpStatus.BAD_REQUEST);
  }
}
