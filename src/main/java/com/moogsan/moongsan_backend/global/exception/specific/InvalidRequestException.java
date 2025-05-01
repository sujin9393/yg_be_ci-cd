package com.moogsan.moongsan_backend.global.exception.specific;

import com.moogsan.moongsan_backend.global.exception.base.BusinessException;
import com.moogsan.moongsan_backend.global.exception.code.ErrorCode;

public class InvalidRequestException extends BusinessException {
    public InvalidRequestException() {
        super(ErrorCode.INVALID_JSON, "잘못된 요청입니다.");
    }

    public InvalidRequestException(String message) {
        super(ErrorCode.INVALID_JSON, message);
    }
}
