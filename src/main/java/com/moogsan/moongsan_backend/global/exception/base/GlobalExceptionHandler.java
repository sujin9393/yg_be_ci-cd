package com.moogsan.moongsan_backend.global.exception.base;

import com.moogsan.moongsan_backend.domain.WrapperResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.exception.base.GroupBuyException;
import com.moogsan.moongsan_backend.global.dto.ErrorResponse;
import com.moogsan.moongsan_backend.global.exception.code.ErrorCode;
import com.moogsan.moongsan_backend.global.exception.code.ErrorCodeType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

// 글로벌 예외 처리기
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 1) DTO 검증 오류 (400)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // Field 별 메시지 수집
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1  // 같은 필드 중복 시 첫 번째 메시지 사용
                ));

        ErrorResponse body = ErrorResponse.builder()
                .errorCode("INVALID_INPUT")
                .message("입력값이 올바르지 않습니다.")
                .errors(errors)
                .timestamp(ZonedDateTime.from(LocalDateTime.now()))
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 2) 도메인 예외
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse body = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())                    // ErrorCodeType.getCode() 값
                .message(ex.getMessage())                  // 기본 메시지 또는 커스텀 메시지
                .timestamp(ZonedDateTime.now())            // 현재 시각
                .build();

        return ResponseEntity
                .status(ex.getHttpStatus())                // ErrorCodeType.getStatus()
                .body(body);
    }

    // 3) 그 외 모든 예외 (500)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        // 로그에 스택트레이스 남기기
        log.error("Unhandled exception caught:", ex);

        ErrorResponse body = ErrorResponse.builder()
                .errorCode("INTERNAL_ERROR")
                .message("서버에서 알 수 없는 오류가 발생했습니다.")
                .timestamp(ZonedDateTime.from(LocalDateTime.now()))
                .build();
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}