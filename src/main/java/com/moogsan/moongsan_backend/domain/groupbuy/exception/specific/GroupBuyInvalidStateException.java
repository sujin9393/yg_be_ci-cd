package com.moogsan.moongsan_backend.domain.groupbuy.exception.specific;

import com.moogsan.moongsan_backend.domain.groupbuy.exception.base.GroupBuyException;
import com.moogsan.moongsan_backend.domain.groupbuy.exception.code.GroupBuyErrorCode;

public class GroupBuyInvalidStateException extends GroupBuyException {
    public GroupBuyInvalidStateException() {
        super(GroupBuyErrorCode.INVALID_STATE, "종료된 공구에는 요청할 수 없습니다.");
    }

    public GroupBuyInvalidStateException(String message) {
        super(GroupBuyErrorCode.INVALID_STATE, message);
    }
}
