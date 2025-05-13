package com.moogsan.moongsan_backend.domain.user.exception.base;

import com.moogsan.moongsan_backend.domain.user.exception.code.UserErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final UserErrorCode errorCode;
    private final Object data;

    // 기본 메시지 사용
    public UserException(UserErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.data = null;
    }

    // 커스텀 메시지 사용
    public UserException(UserErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.data = null;
    }

    public UserException(UserErrorCode errorCode, String message, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.data = data;
    }
}