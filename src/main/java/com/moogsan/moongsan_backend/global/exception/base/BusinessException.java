package com.moogsan.moongsan_backend.global.exception.base;

import com.moogsan.moongsan_backend.global.exception.code.ErrorCodeType;
import org.springframework.http.HttpStatus;
import java.util.Collections;
import java.util.Map;

public class BusinessException extends RuntimeException {
    private final ErrorCodeType errorCode;
    private final Map<String, Object> parameters;

    // 기본 생성자 (ErrorCode만 사용)
    public BusinessException(ErrorCodeType errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.parameters = Collections.emptyMap();
    }

    // 메시지를 커스터마이징할 때 사용
    public BusinessException(ErrorCodeType errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = Collections.emptyMap();
    }

    // 파라미터 정보가 필요할 때 사용
    public BusinessException(ErrorCodeType errorCode, Map<String, Object> parameters) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    // 메시지와 파라미터를 모두 지정할 때 사용
    public BusinessException(ErrorCodeType errorCode, String message, Map<String, Object> parameters) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getStatus();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}

