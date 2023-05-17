package com.allback.cygiadmin.util.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class BaseException extends RuntimeException {
    private int errorCode;
    private List<String> errorMessage;
    private List<String> errorTrace;
    private HttpStatus httpStatus;

    public BaseException() {
    }

    public BaseException(ErrorMessage errorMessage) {
        this.errorCode = errorMessage.getErrorCode();
        this.errorMessage = new ArrayList<>();
        this.errorMessage.add(errorMessage.getErrorMessage());
        this.httpStatus = errorMessage.getHttpStatus();
        errorTrace = new ArrayList<>();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void appendMsg(String msg) {
        this.errorMessage.add(msg);
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public List<String> getErrorTrace() {
        return errorTrace;
    }

    public void appendTrace(String errorTrace) {
        this.errorTrace.add(errorTrace);
    }
}