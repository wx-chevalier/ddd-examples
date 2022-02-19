package icu.ngte.udma.core.type.exception;

import icu.ngte.udma.core.type.code.ApiErrorCode;
import org.springframework.http.HttpStatus;

/**
 * ForbiddenException.
 *
 * @author lotuc
 */
public class ForbiddenException extends BaseBizException {

  public ForbiddenException() {
    super(HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(String message) {
    super(message, HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(String message, Throwable cause) {
    super(message, cause, HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(String message, ApiErrorCode code) {
    super(message, HttpStatus.FORBIDDEN);
    this.setCode(code);
  }
}
