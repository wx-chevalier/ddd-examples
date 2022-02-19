package icu.ngte.udma.core.type.exception;

import icu.ngte.udma.core.type.code.ApiErrorCode;
import org.springframework.http.HttpStatus;

public class NotAcceptException extends BaseBizException {

  private static final long serialVersionUID = 3032690781257768890L;

  public NotAcceptException() {
    super(HttpStatus.NOT_ACCEPTABLE);
  }

  public NotAcceptException(String message) {
    super(message, HttpStatus.NOT_ACCEPTABLE);
  }

  public NotAcceptException(String message, Throwable cause) {
    super(message, cause, HttpStatus.NOT_ACCEPTABLE);
  }

  public NotAcceptException(String message, ApiErrorCode code) {
    super(message, HttpStatus.NOT_ACCEPTABLE);
    this.setCode(code);
  }
}
