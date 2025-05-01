package com.moogsan.moongsan_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckNicknameResponse {
    private String message;
    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private String isDuplication; // "YES" 또는 "NO"
    }
}