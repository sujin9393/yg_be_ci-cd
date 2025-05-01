package com.moogsan.moongsan_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileResponse {
    private String imageUrl;
    private String nickname;
    private String name;
    private String email;
    private String phoneNumber;
    private String accountBank;
    private String accountNumber;
}