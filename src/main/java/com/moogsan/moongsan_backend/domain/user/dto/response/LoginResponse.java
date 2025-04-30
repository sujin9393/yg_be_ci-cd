package com.moogsan.moongsan_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private Data data;

    public LoginResponse(String message, Long id, String nickname, String accessToken, String refreshToken, Long accessTokenExpireAt, String url) {
        this.message = message;
        this.data = new Data(id, nickname, accessToken, refreshToken, accessTokenExpireAt, url);
    }

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