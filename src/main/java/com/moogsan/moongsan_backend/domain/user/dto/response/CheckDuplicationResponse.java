package com.moogsan.moongsan_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckDuplicationResponse {
    private String isDuplication;   // 닉네임 중복 여부 중복=YES/중복없음=NO
}