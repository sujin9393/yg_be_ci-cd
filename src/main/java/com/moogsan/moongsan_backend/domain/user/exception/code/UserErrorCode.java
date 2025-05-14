package com.moogsan.moongsan_backend.domain.user.exception.code;

import com.moogsan.moongsan_backend.global.exception.code.ErrorCodeType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCodeType {

    // 400 Bad Request
    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),

    // 401 Unauthorized
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    // 403 Forbidden
    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404 Not Found
    NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "요청한 유저를 찾을 수 없습니다."),

    // 409 Conflict
    DUPLICATE_VALUE("DUPLICATE_VALUE", HttpStatus.CONFLICT, "이미 등록된 값이 존재합니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final String code;
    private final HttpStatus status;
    @Getter
    private final String defaultMessage;

    UserErrorCode(String code, HttpStatus status, String defaultMessage) {
        this.code = code;
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

}
