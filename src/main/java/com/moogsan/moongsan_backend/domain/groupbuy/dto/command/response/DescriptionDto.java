package com.moogsan.moongsan_backend.domain.groupbuy.dto.command.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DescriptionDto(
        String message,
        Data data
) {
    public record Data(
            @JsonProperty("product_name")
            String productName,

            @JsonProperty("product_lower_name")
            String productLowerName,

            @JsonProperty("total_price")
            int totalPrice,

            int count,

            String summary
    ) {}
}
