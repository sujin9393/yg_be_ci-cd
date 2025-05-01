package com.moogsan.moongsan_backend.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 전역 예외 처리를 담당하는 핸들러 클래스.
 * 컨트롤러 단에서 발생하는 예외를 일관된 형식으로 응답한다.
 */
@RestControllerAdvice // 전역적으로 모든 컨트롤러의 예외를 가로채서 처리하는 어노테이션
public class UserExceptionHandler {

    /**
     * IllegalArgumentException이 발생할 경우 409 Conflict 상태로 응답하는 메서드.
     *
     * @param e 발생한 IllegalArgumentException 객체
     * @return 409 Conflict 상태 코드와 함께 에러 메시지를 포함한 응답 본문
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // HTTP 409 Conflict 상태 반환
                .body(new ErrorResponse(e.getMessage())); // 에러 메시지를 담은 응답 본문 반환
    }

    /**
     * 에러 응답을 표준화하기 위한 내부 static 클래스.
     * 단순히 에러 메시지 하나만을 반환한다.
     */
    static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}