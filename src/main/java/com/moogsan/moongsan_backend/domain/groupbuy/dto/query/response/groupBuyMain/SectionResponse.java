package com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyMain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SectionResponse {

    private String code;        // endSoon, latest, moongsanPick
    private String displayName; // 마감 임박!!, 전체, 뭉산 PICK 핫 공구

    private List<MainSummaryResponse> posts;
}
