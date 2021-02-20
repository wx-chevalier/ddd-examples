package com.udma.core.type.exception;

public class DataValidationException extends RuntimeException {

  public DataValidationException() {}

  public DataValidationException(String message) {
    super(message);
  }

  public DataValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataValidationException(Throwable cause) {
    super(cause);
  }

  public DataValidationException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
