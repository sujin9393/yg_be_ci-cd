package com.moogsan.moongsan_backend.domain.user.exception.handler;

import com.moogsan.moongsan_backend.domain.user.exception.base.UserException;
import com.moogsan.moongsan_backend.domain.user.exception.code.UserErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.moogsan.moongsan_backend.domain.user")
public class UserExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getMessage(), e.getData()));
    }

    @Getter
    public static class ErrorResponse {
        private final String message;
        private final Object data;

        public ErrorResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }
    }
}