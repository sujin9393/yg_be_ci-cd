package com.moogsan.moongsan_backend.global.exception.specific;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}