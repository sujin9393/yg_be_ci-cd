package com.moogsan.moongsan_backend.domain.groupbuy.controller;

import com.moogsan.moongsan_backend.domain.WrapperResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyDetail.DetailResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.BasicList.BasicListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.HostedList.HostedListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.PagedResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.ParticipantList.ParticipantListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.ParticipatedList.ParticipatedListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.WishList.WishListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyUpdate.GroupBuyForUpdateResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyMain.MainPageResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.service.GroupBuyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group-buys")
public class GroupBuyQueryController {
    private final GroupBuyQueryService groupBuyService;

    // 공구 게시글 수정 전 정보 조회
    @GetMapping("/{postId}/for-edit")
    public ResponseEntity<WrapperResponse<GroupBuyForUpdateResponse>> getGroupBuyEditInfo(@PathVariable Long postId) {
        GroupBuyForUpdateResponse groupBuyForUpdate = groupBuyService.getGroupBuyEditInfo(postId);
        return ResponseEntity.ok(
                WrapperResponse.<GroupBuyForUpdateResponse>builder()
                        .message("공구 게시글 수정을 성공적으로 조회했습니다.")
                        .data(groupBuyForUpdate)
                        .build()
        );
    }

    // 메인 페이지 조회
    @GetMapping("/main")
    public ResponseEntity<WrapperResponse<MainPageResponse>> getGroupBuyMainInfo() {
        MainPageResponse groupBuyMainPage = groupBuyService.getGroupBuyMainInfo();

        return ResponseEntity.ok(
                WrapperResponse.<MainPageResponse>builder()
                        .message("메인 화면을 성공적으로 조회했습니다.")
                        .data(groupBuyMainPage)
                        .build()
        );
    }

    // 공구 리스트 조회
    @GetMapping
    public ResponseEntity<WrapperResponse<PagedResponse<BasicListResponse>>> getGroupBuyBasicList(
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        PagedResponse<BasicListResponse> pagedResponse = groupBuyService.getGroupBuyBasicList(categoryId, sort, cursor, limit);
        return ResponseEntity.ok(
                WrapperResponse.<PagedResponse<BasicListResponse>>builder()
                        .message("전체 리스트를 성공적으로 조회했습니다.")
                        .data(pagedResponse)
                        .build()
        );
    }

    // 공구 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<WrapperResponse<DetailResponse>> getGroupBuyDetailInfo(@PathVariable Long postId) {
        DetailResponse detail = groupBuyService.getGroupBuyDetailInfo(postId);
        return ResponseEntity.ok(
                WrapperResponse.<DetailResponse>builder()
                        .message("공구 게시글 상세 정보를 성공적으로 조회했습니다.")
                        .data(detail)
                        .build()
        );
    }

    // 관심 공구 리스트 조회
    @GetMapping("/user/wishes")
    public ResponseEntity<WrapperResponse<PagedResponse<WishListResponse>>> getGroupBuyWishList(
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        PagedResponse<WishListResponse> pagedResponse = groupBuyService.getGroupBuyWishList(cursor, limit);
        return ResponseEntity.ok(
                WrapperResponse.<PagedResponse<WishListResponse>>builder()
                        .message("관심 공구 리스트를 성공적으로 조회했습니다.")
                        .data(pagedResponse)
                        .build()
        );
    }

    // 주최 공구 리스트 조회
    @GetMapping("/user/hosts")
    public ResponseEntity<WrapperResponse<PagedResponse<HostedListResponse>>> getGroupBuyHostedList(
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        PagedResponse<HostedListResponse> pagedResponse = groupBuyService.getGroupBuyHostedList(sort, cursor, limit);
        return ResponseEntity.ok(
                WrapperResponse.<PagedResponse<HostedListResponse>>builder()
                        .message("주최 공구 리스트를 성공적으로 조회했습니다.")
                        .data(pagedResponse)
                        .build()
        );
    }

    // 참여 공구 리스트 조회
    /*
    @GetMapping("/user/participants")
    public ResponseEntity<WrapperResponse<PagedResponse<ParticipatedListResponse>>> getGroupBuyParticipatedList(
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        PagedResponse<ParticipatedListResponse> pagedResponse = groupBuyService.getGroupBuyParticipatedList(sort, cursor, limit);
        return ResponseEntity.ok(
                WrapperResponse.<PagedResponse<ParticipatedListResponse>>builder()
                        .message("참여 공구 리스트를 성공적으로 조회했습니다.")
                        .data(pagedResponse)
                        .build()
        );
    }
    */

    // 공구 참여자 조회
    @GetMapping("/{postId}/participants")
    public ResponseEntity<WrapperResponse<ParticipantListResponse>> getGroupBuyParticipantsInfo(@PathVariable Long postId) {
        ParticipantListResponse participantList = groupBuyService.getGroupBuyParticipantsInfo(postId);
        return ResponseEntity.ok(
                WrapperResponse.<ParticipantListResponse>builder()
                        .message("공구 참여자 리스트를 성공적으로 조회했습니다.")
                        .data(participantList)
                        .build()
        );
    }
}
