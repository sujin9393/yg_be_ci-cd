package com.moogsan.moongsan_backend.global.exception.specific;

import com.moogsan.moongsan_backend.global.exception.base.BusinessException;
import com.moogsan.moongsan_backend.global.exception.code.ErrorCode;

public class InternalServerErrorException extends BusinessException {
    public InternalServerErrorException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }

    public InternalServerErrorException(String message) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message);
    }
}