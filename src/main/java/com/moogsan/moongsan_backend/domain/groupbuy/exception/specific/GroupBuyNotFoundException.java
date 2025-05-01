package com.moogsan.moongsan_backend.domain.groupbuy.exception;

import com.moogsan.moongsan_backend.domain.groupbuy.exception.base.GroupBuyException;
import com.moogsan.moongsan_backend.domain.groupbuy.exception.code.GroupBuyErrorCode;

public class GroupBuyNotFoundException extends GroupBuyException {
    public GroupBuyNotFoundException() {
        super(GroupBuyErrorCode.GROUPBUY_NOT_FOUND, "존재하지 않는 공구입니다.");
    }

    public GroupBuyNotFoundException(String message) {
        super(GroupBuyErrorCode.GROUPBUY_NOT_FOUND, message);
    }
}
