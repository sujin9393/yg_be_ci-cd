package com.moogsan.moongsan_backend.domain.groupbuy.exception.specific;

import com.moogsan.moongsan_backend.domain.groupbuy.exception.base.GroupBuyException;
import com.moogsan.moongsan_backend.domain.groupbuy.exception.code.GroupBuyErrorCode;

public class GroupBuyNotHostException extends GroupBuyException {
    public GroupBuyNotHostException() {
        super(GroupBuyErrorCode.NOT_HOST, "공구의 주최자만 요청 가능합니다.");
    }

    public GroupBuyNotHostException(String message) {
        super(GroupBuyErrorCode.NOT_HOST, message);
    }
}
