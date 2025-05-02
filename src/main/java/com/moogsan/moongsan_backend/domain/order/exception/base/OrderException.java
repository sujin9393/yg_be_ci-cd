package com.moogsan.moongsan_backend.domain.order.exception.base;

import com.moogsan.moongsan_backend.domain.order.exception.code.OrderErrorCode;
import com.moogsan.moongsan_backend.global.exception.base.BusinessException;

import java.util.Map;

public class OrderException extends BusinessException {
    public OrderException(OrderErrorCode errorCode) {
        super(errorCode);
    }

    public OrderException(OrderErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public OrderException(OrderErrorCode errorCode, Map<String, Object> parameters) {
        super(errorCode, parameters);
    }

    public OrderException(OrderErrorCode errorCode, String message, Map<String, Object> parameters) {
        super(errorCode, message, parameters);
    }
}