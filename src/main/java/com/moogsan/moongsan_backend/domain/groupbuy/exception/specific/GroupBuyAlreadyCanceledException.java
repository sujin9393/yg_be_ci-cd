package com.moogsan.moongsan_backend.domain.groupbuy.exception.specific;

import com.moogsan.moongsan_backend.domain.groupbuy.exception.base.GroupBuyException;
import com.moogsan.moongsan_backend.domain.groupbuy.exception.code.GroupBuyErrorCode;

public class GroupBuyAlreadyCanceledException extends GroupBuyException {
    public GroupBuyAlreadyCanceledException() {
        super(GroupBuyErrorCode.ALREADY_CANCELED, "공구에 대한 참여가 이미 취소된 상태입니다.");
    }

    public GroupBuyAlreadyCanceledException(String message) {
        super(GroupBuyErrorCode.ALREADY_CANCELED, message);
    }
}
