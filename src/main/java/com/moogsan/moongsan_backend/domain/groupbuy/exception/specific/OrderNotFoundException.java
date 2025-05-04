package com.moogsan.moongsan_backend.domain.groupbuy.exception.specific;

import com.moogsan.moongsan_backend.domain.groupbuy.exception.base.GroupBuyException;
import com.moogsan.moongsan_backend.domain.groupbuy.exception.code.GroupBuyErrorCode;

public class OrderNotFoundException extends GroupBuyException {
    public OrderNotFoundException() {
        super(GroupBuyErrorCode.ORDER_NOT_FOUND, "존재하지 않는 주문입니다.");
    }

    public OrderNotFoundException(String message) {
        super(GroupBuyErrorCode.ORDER_NOT_FOUND, message);
    }
}