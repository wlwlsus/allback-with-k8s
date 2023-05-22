package com.allback.cygiuser.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorMessage {

  ACCESS_TOKEN_INVALID_SIGNATURE(1013, "토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
  NOT_EXIST_USER(1, "유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
  NOT_ENOUGH_CASH(2, "금액이 부족합니다.", HttpStatus.BAD_REQUEST),
  FAILED_TO_SAVE_USER_INFO(3, "정보 저장에 실패하였습니다.", HttpStatus.BAD_REQUEST);
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
