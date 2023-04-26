package com.allback.cygiconcert.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

public class FileUploadFailedException extends Exception {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<?> handleMaxUploadSizeExceededException(
        MaxUploadSizeExceededException e) {
        String msg = "파일크기를 초과했습니다. ";
        return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);
    }
}
