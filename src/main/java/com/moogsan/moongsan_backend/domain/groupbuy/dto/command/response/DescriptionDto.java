package com.moogsan.moongsan_backend.domain.groupbuy.dto.command.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DescriptionDto(
        @JsonProperty("title") String title,
        @JsonProperty("product_name") String productName,
        @JsonProperty("total_price") int totalPrice,
        int count,
        String summary
) {}

