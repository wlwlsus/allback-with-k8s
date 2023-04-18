package com.allback.cygiadmin.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * How to use 1. Make Controller's return type 'ResponseEntity<?>' 2. Use
 * Response.makeResponse(HttpStatus, message, result)
 */

public class Response {

    @Getter
    @Builder
    private static class Body {

        private String message;
        private Object result;
    }

    public static ResponseEntity<?> makeResponse(HttpStatus httpStatus, String message,
        Object result) {
        Body body = Body.builder()
            .message(message)
            .result(result)
            .build();

        return new ResponseEntity<>(body, httpStatus);
    }

    public static ResponseEntity<?> makeResponse(HttpStatus httpStatus, String message) {
        return makeResponse(httpStatus, message, null);
    }


    // 200
    public static ResponseEntity<?> ok(String message) {
        return makeResponse(HttpStatus.OK, message, null);
    }

    // 201
    public static ResponseEntity<?> created(String message) {
        return makeResponse(HttpStatus.CREATED, message, null);
    }

    // 400
    public static ResponseEntity<?> badRequest(String message) {
        return makeResponse(HttpStatus.BAD_REQUEST, message, null);
    }

    // 401
    public static ResponseEntity<?> noContent(String message) {
        return makeResponse(HttpStatus.NO_CONTENT, message, null);
    }

    // 404
    public static ResponseEntity<?> notFound(String message) {
        return makeResponse(HttpStatus.NOT_FOUND, message, null);
    }

    // 500
    public static ResponseEntity<?> serverError(String message) {
        return makeResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }
}