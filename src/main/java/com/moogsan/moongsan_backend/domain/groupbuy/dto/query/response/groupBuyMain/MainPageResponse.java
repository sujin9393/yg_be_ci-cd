package com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyMain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MainPageResponse {

    private List<SectionResponse> boards;

}
