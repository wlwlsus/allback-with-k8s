package com.allback.cygiuser.util.exception;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<?> baseHandler(BaseException e) {
    Map<String, Object> result = new HashMap<>();
    if (e.getErrorCode() != 0) {
      result.put("result", false);
      result.put("msg", e.getErrorMessage());
    }
    return new ResponseEntity<Object>(result, e.getHttpStatus());
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<?> handler(Throwable t) {
    t.printStackTrace();
    logger.error(t.getMessage());

    return new ResponseEntity<Object>(new HashMap<String, Object>() {{
      put("result", false);
      put("msg", t.getMessage());
    }}, HttpStatus.INTERNAL_SERVER_ERROR);

  }

  @NoArgsConstructor
  @AllArgsConstructor
  static class Error {
    private int code;
    private HttpStatus status;
    private List<String> message;
    private List<String> trace;

    static Error create(BaseException exception) {
      return new Error(exception.getErrorCode(), exception.getHttpStatus(), exception.getErrorMessage(), exception.getErrorTrace());
    }

    public int getCode() {
      return code;
    }

    public HttpStatus getStatus() {
      return status;
    }

    public List<String> getMessage() {
      return message;
    }

    public List<String> getTrace() {
      return trace;
    }
  }
}