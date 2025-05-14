package com.moogsan.moongsan_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponse { // 현재 회원가입에서 로그인리스폰스 사용
    private Long userId;        // user ID
    private String nickname;    // 닉네임
    private String accessToken; // 액세스 토큰
    private String refreshToken;// 리프레시 토큰
    private Long accessTokenExpireAt;   // 액세스 토큰 만료일자
    private String redirectUrl; // 리다이렉트 URL
}