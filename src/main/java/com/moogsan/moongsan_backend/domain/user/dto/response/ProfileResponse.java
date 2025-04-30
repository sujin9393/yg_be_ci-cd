package com.moogsan.moongsan_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    private String message;
    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private String imageUrl;
        private String nickname;
        private String name;
        private String email;
        private String phoneNumber;
        private String accountBank;
        private String accountNumber;
    }
}