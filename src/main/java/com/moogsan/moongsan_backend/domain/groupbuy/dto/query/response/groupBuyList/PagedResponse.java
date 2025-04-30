package com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PagedResponse<T> {

    private long count;     // 공구 게시글 개수
    private List<T> posts;       // 공구 게시글 리스트

    private Integer nextCursor;  // 현재 응답의 마지막 postId. null 여부를 표현하기 위해 Integer 사용
    private boolean hasMore;     // 다음 페이지 존재 여부

}
