package com.moogsan.moongsan_backend.domain.groupbuy.controller;

import com.moogsan.moongsan_backend.domain.EmptyResponse;
import com.moogsan.moongsan_backend.domain.WrapperResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.command.request.CreateGroupBuyRequest;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.command.response.CommandGroupBuyResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyUpdate.GroupBuyForUpdateResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.service.GroupBuyCommandService;
import com.moogsan.moongsan_backend.domain.groupbuy.service.GroupBuyQueryService;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group-buys")
public class GroupBuyCommandController {

    private final GroupBuyCommandService groupBuyService;

    // 공구 게시글 작성
    @PostMapping
    public ResponseEntity<WrapperResponse<CommandGroupBuyResponse>> createGroupBuy(
            @AuthenticationPrincipal User currentUser,
            @Valid @ModelAttribute CreateGroupBuyRequest createGroupBuyRequest) {

        Long postId = groupBuyService.createGroupBuy(currentUser, createGroupBuyRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postId)
                .toUri();

        CommandGroupBuyResponse commandGroupBuy = new CommandGroupBuyResponse(postId);

        return ResponseEntity
                .created(location)
                .body(WrapperResponse.<CommandGroupBuyResponse>builder()
                        .message("공구 게시글이 성공적으로 업로드되었습니다.")
                        .data(commandGroupBuy)
                        .build());
    }

    // 공구 게시글 수정
    //  TODO V2

    // 공구 게시글 삭제
    //  TODO V2

    // 공구 참여 취소
    @DeleteMapping("/{postId}/participants")
    public ResponseEntity<WrapperResponse<EmptyResponse>> leaveGroupBuy(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long postId){
        groupBuyService.leaveGroupBuy(currentUser, postId);

        return ResponseEntity.ok(
                WrapperResponse.<EmptyResponse>builder()
                        .message("공구 참여가 성공적으로 취소되었습니다.")
                        .build()
        );
    }

    // 관심 공구 추가
    //  TODO V2

    // 관심 공구 취소
    //  TODO V2

    // 공구 게시글 공구 종료
    @PatchMapping("/{postId}/participants")
    public ResponseEntity<WrapperResponse<EmptyResponse>> endGroupBuy(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long postId){
        groupBuyService.endGroupBuy(currentUser, postId);

        return ResponseEntity.ok(
                WrapperResponse.<EmptyResponse>builder()
                        .message("공구가 성공적으로 종료되었습니다.")
                        .build()
        );
    }

    // 검색 -> post가 아니라 get이어야 하지 않나?
    //  TODO V2

}
