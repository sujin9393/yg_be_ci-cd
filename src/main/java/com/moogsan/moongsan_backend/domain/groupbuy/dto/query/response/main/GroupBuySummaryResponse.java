package com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.main;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupBuySummaryResponse {

    // 식별/메타
    private Long postId;        // 공구 게시글 아이디
    private String title;       // 제목
    private String postStatus;  // 공구 진행 상태

    // 본문
    private String imageUrl;    // thumbnail 이미지

    // 숫자 데이터
    private int unitPrice;      // 단위 당 가격
    private int unitAmount;     // 상품 주문 단위
    private int soldAmount;     // 판매 수량: totalAmount - leftAmount
    private int totalAmount;    // 상품 전체 수량

    // 상태/플래그
    private boolean isWish;     // 관심 여부

}
