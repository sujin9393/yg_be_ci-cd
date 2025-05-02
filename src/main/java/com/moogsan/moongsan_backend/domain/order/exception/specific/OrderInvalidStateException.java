package com.moogsan.moongsan_backend.domain.order.exception.specific;

import com.moogsan.moongsan_backend.domain.order.exception.base.OrderException;
import com.moogsan.moongsan_backend.domain.order.exception.code.OrderErrorCode;

public class OrderInvalidStateException extends OrderException {
    public OrderInvalidStateException() {
        super(OrderErrorCode.INVALID_STATE, "이미 취소된 주문입니다");
    }

    public OrderInvalidStateException(String message) {
        super(OrderErrorCode.INVALID_STATE, message);
    }
}
