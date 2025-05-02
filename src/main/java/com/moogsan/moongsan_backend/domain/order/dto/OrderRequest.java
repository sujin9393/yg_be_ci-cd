package com.moogsan.moongsan_backend.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {

    @NotBlank
    private int userId;

    @NotBlank
    private int postId;

    @NotBlank
    private long quantity;

    private String name;
}
