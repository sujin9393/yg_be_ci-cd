package com.moogsan.moongsan_backend.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WishRequest {

    @NotNull
    private Long postId; // 공구글 ID
}