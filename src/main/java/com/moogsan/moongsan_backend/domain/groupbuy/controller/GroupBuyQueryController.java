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
import com.moogsan.moongsan_backend.domain.groupbuy.service.GroupBuyQueryService;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    // 공구 리스트 조회
    @GetMapping
    public ResponseEntity<WrapperResponse<PagedResponse<BasicListResponse>>> getGroupBuyListByCursor(
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "sort", defaultValue = "created") String sort,
            @RequestParam(value = "cursor", required = false) Long cursor,
            // 커서 페이징용 추가 파라미터들
            @RequestParam(value = "lastCreatedAt", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime lastCreatedAt,
            @RequestParam(value = "lastPrice", required = false) Integer lastPrice,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        PagedResponse<BasicListResponse> pagedResponse =
                groupBuyService.getGroupBuyListByCursor(categoryId, sort, cursor, lastCreatedAt,
                                                        lastPrice, limit);
        return ResponseEntity.ok(
                WrapperResponse.<PagedResponse<BasicListResponse>>builder()
                        .message("전체 리스트를 성공적으로 조회했습니다.")
                        .data(pagedResponse)
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

    /*
    // 참여 공구 리스트 조회
    @GetMapping("/user/participants")
    public ResponseEntity<WrapperResponse<PagedResponse<ParticipatedListResponse>>> getGroupBuyParticipatedList(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "cursor", required = false) Long cursor,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit
    ) {
        PagedResponse<ParticipatedListResponse> pagedResponse = groupBuyService.getGroupBuyParticipatedList(currentUser, sort, cursor, limit);
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
    public ResponseEntity<WrapperResponse<ParticipantListResponse>> getGroupBuyParticipantsInfo(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long postId) {
        ParticipantListResponse participantList = groupBuyService.getGroupBuyParticipantsInfo(postId);
        return ResponseEntity.ok(
                WrapperResponse.<ParticipantListResponse>builder()
                        .message("공구 참여자 리스트를 성공적으로 조회했습니다.")
                        .data(participantList)
                        .build()
        );
    }
}
