package com.moogsan.moongsan_backend.global.exception.base;

import com.moogsan.moongsan_backend.global.dto.ErrorResponse;
import com.moogsan.moongsan_backend.global.exception.code.ErrorCode;
import com.moogsan.moongsan_backend.global.exception.code.ErrorCodeType;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.BindingResult;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

// 글로벌 예외 처리기
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

    // @Valid 검증 실패
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ErrorResponse> handleValidation(Exception ex) {
        BindingResult result = (ex instanceof MethodArgumentNotValidException manv)
                ? manv.getBindingResult()
                : ((BindException) ex).getBindingResult();

        Map<String, String> fieldErrors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage(),
                        (existing, replacement) -> existing
                ));

        ErrorResponse body = new ErrorResponse(
                ErrorCode.VALIDATION_FAILED.getCode(),
                "입력 값 검증에 실패했습니다.",
                ZonedDateTime.now(),
                Map.of("fields", fieldErrors),
                getTraceId()
        );
        return new ResponseEntity<>(body, ErrorCode.VALIDATION_FAILED.getStatus());
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

    // 핸들러 미매핑 (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex) {
        ErrorResponse body = new ErrorResponse(
                ErrorCode.NOT_FOUND.getCode(),
                "해당 리소스를 찾을 수 없습니다.",
                ZonedDateTime.now(),
                null,
                getTraceId()
        );
        return new ResponseEntity<>(body, ErrorCode.NOT_FOUND.getStatus());
    }

    // 기타 예외 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest request) {
        // log.error("Unhandled exception", ex);
        ErrorResponse body = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                "서버에서 알 수 없는 오류가 발생했습니다.",
                ZonedDateTime.now(),
                null,
                getTraceId()
        );
        return new ResponseEntity<>(body, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}