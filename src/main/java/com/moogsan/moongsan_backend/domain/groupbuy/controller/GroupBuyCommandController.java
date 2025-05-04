package com.moogsan.moongsan_backend.domain.groupbuy.controller;

import com.moogsan.moongsan_backend.domain.EmptyResponse;
import com.moogsan.moongsan_backend.domain.WrapperResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.command.request.CreateGroupBuyRequest;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.command.request.UpdateGroupBuyRequest;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.command.response.CommandGroupBuyResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyUpdate.GroupBuyForUpdateResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.service.GroupBuyCommandService;
import com.moogsan.moongsan_backend.domain.groupbuy.service.GroupBuyQueryService;
import com.moogsan.moongsan_backend.domain.user.entity.CustomUserDetails;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group-buys")
public class GroupBuyCommandController {

    private final GroupBuyCommandService groupBuyService;

    /// 공구 게시글 작성 SUCCESS
    @PostMapping
    public ResponseEntity<WrapperResponse<CommandGroupBuyResponse>> createGroupBuy(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateGroupBuyRequest request) {

        Long postId = groupBuyService.createGroupBuy(userDetails.getUser(), request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postId)
                .toUri();

        CommandGroupBuyResponse response = new CommandGroupBuyResponse(postId);
        return ResponseEntity.created(location)
                .body(WrapperResponse.<CommandGroupBuyResponse>builder()
                        .message("공구 게시글이 성공적으로 업로드되었습니다.")
                        .data(response)
                        .build());
    }

    // 공구 게시글 수정
    //  TODO V2
    @PatchMapping("{postId}")
    public ResponseEntity<WrapperResponse<CommandGroupBuyResponse>> updateGroupBuy(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateGroupBuyRequest request,
            @PathVariable Long postId) {

        groupBuyService.updateGroupBuy(userDetails.getUser(), request, postId);

        CommandGroupBuyResponse response = new CommandGroupBuyResponse(postId);

        return ResponseEntity.ok()
                .body(WrapperResponse.<CommandGroupBuyResponse>builder()
                        .message("공구 게시글이 성공적으로 수정되었습니다.")
                        .data(response)
                        .build());
    }

    // 공구 게시글 삭제
    //  TODO V2


    // 공구 참여 취소
    @DeleteMapping("/{postId}/participants")
    public ResponseEntity<WrapperResponse<EmptyResponse>> leaveGroupBuy(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId){
        groupBuyService.leaveGroupBuy(userDetails.getUser(), postId);

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
    @PatchMapping("/{postId}/end")
    public ResponseEntity<WrapperResponse<EmptyResponse>> endGroupBuy(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId){
        groupBuyService.endGroupBuy(userDetails.getUser(), postId);

        return ResponseEntity.ok(
                WrapperResponse.<EmptyResponse>builder()
                        .message("공구가 성공적으로 종료되었습니다.")
                        .build()
        );
    }

    // 검색 -> post가 아니라 get이어야 하지 않나?
    //  TODO V2

}
