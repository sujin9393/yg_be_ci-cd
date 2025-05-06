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
        UserErrorCode errorCode = e.getErrorCode();
        String message = e.getMessage(); // already uses defaultMessage if custom not provided

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), message));
    }

    @Getter
    public static class ErrorResponse {
        private final String code;
        private final String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }
}
