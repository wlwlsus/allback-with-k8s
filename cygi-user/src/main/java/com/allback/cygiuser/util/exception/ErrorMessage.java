package com.allback.cygiuser.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {

  ACCESS_TOKEN_INVALID_SIGNATURE(1013, "토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);

  private final Integer code;
  private final String errMsg;
  private final HttpStatus httpStatus;

  ErrorMessage(int code, String errMsg, HttpStatus httpStatus) {
    this.code = code;
    this.errMsg = errMsg;
    this.httpStatus = httpStatus;
  }

  public int getErrorCode() {
    return code;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getErrorMessage() {
    return errMsg;
  }
}
