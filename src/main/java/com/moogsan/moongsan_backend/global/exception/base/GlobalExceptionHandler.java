package com.moogsan.moongsan_backend.global.exception.base;

import com.moogsan.moongsan_backend.domain.WrapperResponse;
import com.moogsan.moongsan_backend.global.dto.ErrorResponse;
import com.moogsan.moongsan_backend.global.exception.code.ErrorCode;
import com.moogsan.moongsan_backend.global.exception.code.ErrorCodeType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.time.ZonedDateTime;

import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

// 글로벌 예외 처리기
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private String getTraceId() {
        return MDC.get("traceId");
    }

    // 비즈니스 예외
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        ErrorCodeType ec = ex.getErrorCode();
        ErrorResponse body = new ErrorResponse(
                ec.getCode(),
                ex.getMessage(),
                ZonedDateTime.now(),
                null,
                getTraceId()
        );
        return new ResponseEntity<>(body, ec.getStatus());
    }

    // JSON 파싱 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleParsing(HttpMessageNotReadableException ex) {
        ErrorResponse body = new ErrorResponse(
                ErrorCode.INVALID_JSON.getCode(),
                "잘못된 JSON 포맷입니다.",
                ZonedDateTime.now(),
                null,
                getTraceId()
        );
        return new ResponseEntity<>(body, ErrorCode.INVALID_JSON.getStatus());
    }

    // 지원하지 않는 HTTP 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ErrorResponse body = new ErrorResponse(
                ErrorCode.METHOD_NOT_ALLOWED.getCode(),
                ex.getMethod() + " 메서드는 지원하지 않습니다.",
                ZonedDateTime.now(),
                null,
                getTraceId()
        );
        return new ResponseEntity<>(body, ErrorCode.METHOD_NOT_ALLOWED.getStatus());
    }

    // 검증 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WrapperResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity
                .badRequest()
                .body(WrapperResponse.<Void>builder().message(msg).data(null).build());
    }

    // 데이터 무결성 위반 (예: NOT NULL 제약)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<WrapperResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(WrapperResponse.<Void>builder()
                        .message("요청 데이터에 제약 위반이 발생했습니다.")
                        .data(null)
                        .build());
    }

    // 엔티티를 못 찾았을 때
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<WrapperResponse<Void>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(WrapperResponse.<Void>builder()
                        .message(ex.getMessage())
                        .data(null)
                        .build());
    }

    /*

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        // 예외 스택트레이스 로깅
        log.error("Unhandled exception caught in GlobalExceptionHandler", ex);

        // INTERNAL_SERVER_ERROR 코드 및 메시지, 스택트레이스는 details 에 담아서 반환
        ErrorResponse body = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),       // "INTERNAL_SERVER_ERROR"
                "서버에서 알 수 없는 오류가 발생했습니다.",
                ZonedDateTime.now(),
                Map.of("stackTrace", Arrays.toString(ex.getStackTrace())),
                getTraceId()
        );

        return new ResponseEntity<>(body, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }
     */
}