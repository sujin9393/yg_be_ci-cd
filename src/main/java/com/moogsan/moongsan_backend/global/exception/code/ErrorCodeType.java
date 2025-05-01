package com.moogsan.moongsan_backend.global.exception.code;

import org.springframework.http.HttpStatus;

public interface ErrorCodeType {
    String   getCode();
    HttpStatus getStatus();
}
