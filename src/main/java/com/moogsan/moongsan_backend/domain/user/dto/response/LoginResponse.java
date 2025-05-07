package com.moogsan.moongsan_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String nickname;
    private String name;
    private String imageUrl;
    private String type;
}