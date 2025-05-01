package com.moogsan.moongsan_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponse {
    private String message;
    private Data data;

    @Getter
    @AllArgsConstructor
    public static class Data {
        private Long userId;
        private String nickname;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExpireAt;
        private String redirectUrl;
    }
}