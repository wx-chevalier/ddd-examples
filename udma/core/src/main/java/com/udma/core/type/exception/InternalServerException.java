package com.udma.core.type.exception;

import com.udma.core.type.code.ApiErrorCode;
import org.springframework.http.HttpStatus;

/**
 * InternalServerException.
 *
 * @author lotuc
 */
public class InternalServerException extends
    BaseBizException {

  public InternalServerException() {
    super(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public InternalServerException(String message) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public InternalServerException(String message, Throwable cause) {
    super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public InternalServerException(String message, ApiErrorCode code) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    this.setCode(code);
  }
}
