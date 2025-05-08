package com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyDetail;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {

    // 식별/메타
    private Long authorId;                // 작성자 아이디
    private String nickname;              // 닉네임

    // 본문
    private String accountNumber;         // 계좌 번호
    private String accountBank;           // 은행
    private String profileImageUrl;       // 프로필 이미지

}
