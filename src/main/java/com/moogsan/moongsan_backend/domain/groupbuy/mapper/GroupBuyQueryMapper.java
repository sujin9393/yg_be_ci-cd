package com.moogsan.moongsan_backend.domain.groupbuy.mapper;

import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.ImageResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyDetail.DetailResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyDetail.UserProfileResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.BasicList.BasicListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.ParticipatedList.ParticipatedListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyUpdate.GroupBuyForUpdateResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import com.moogsan.moongsan_backend.domain.image.entity.Image;
import com.moogsan.moongsan_backend.domain.order.entity.Order;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupBuyQueryMapper {

    // 수정 정보 조회용 DTO 변환
    public GroupBuyForUpdateResponse toUpdateResponse(GroupBuy gb){
        List<ImageResponse> imageUrls = gb.getImages().stream()
                .map(image -> ImageResponse.builder()
                        .imageKey(image.getImageKey())
                        .imageSeqNo(image.getImageSeqNo())
                        .thumbnail(image.isThumbnail())
                        .build())
                .toList();

        return GroupBuyForUpdateResponse.builder()
                .title(gb.getTitle())
                .name(gb.getName())
                .description(gb.getDescription())
                .url(gb.getUrl())
                .imageKeys(imageUrls)
                .dueDate(gb.getDueDate())
                .location(gb.getLocation())
                .pickupDate(gb.getPickupDate())
                .price(gb.getPrice())
                .unitAmount(gb.getUnitAmount())
                .totalAmount(gb.getTotalAmount())
                .build();
    }

    // 공구 리스트 조회용 DTO 변환
    public BasicListResponse toBasicListResponse(GroupBuy g) {
        List<ImageResponse> imgs = g.getImages().stream()
                .map(img -> ImageResponse.builder()
                        .imageKey(img.getImageKey())
                        .imageSeqNo(img.getImageSeqNo())
                        .thumbnail(img.isThumbnail())
                        .build())
                .collect(Collectors.toList());

        return BasicListResponse.builder()
                .postId(g.getId())
                .title(g.getTitle())
                .name(g.getName())
                .postStatus(g.getPostStatus())
                .imageKeys(imgs)
                .unitPrice(g.getUnitPrice())
                .unitAmount(g.getUnitAmount())
                .soldAmount(g.getTotalAmount() - g.getLeftAmount())
                .totalAmount(g.getTotalAmount())
                .participantCount(g.getParticipantCount())
                .dueSoon(g.isDueSoon())
                .createdAt(g.getCreatedAt())
                .build();
    }

    // 상세 페이지 조회용 DTO
    public DetailResponse toDetailResponse(GroupBuy gb) {
        List<ImageResponse> imageUrls = gb.getImages().stream()
                .map(img -> ImageResponse.builder()
                        .imageKey(img.getImageKey())
                        .imageSeqNo(img.getImageSeqNo())
                        .thumbnail(img.isThumbnail())
                        .build())
                .toList();

        return DetailResponse.builder()
                .postId(gb.getId())
                .title(gb.getTitle())
                .name(gb.getName())
                .postStatus(gb.getPostStatus())
                .description(gb.getDescription())
                .url(gb.getUrl())
                .imageKeys(imageUrls)
                .unitPrice(gb.getUnitPrice())
                .unitAmount(gb.getUnitAmount())
                .soldAmount(gb.getTotalAmount() - gb.getLeftAmount())
                .totalAmount(gb.getTotalAmount())
                .participantCount(gb.getParticipantCount())
                .dueDate(gb.getDueDate())
                .pickupDate(gb.getPickupDate())
                .location(gb.getLocation())
                .createdAt(gb.getCreatedAt())
                .userProfileResponse(toUserProfile(gb.getUser()))
                .build();
    }

    // 공동구매 유저 프로필 조회용 DTO
    private UserProfileResponse toUserProfile(User u) {
        return UserProfileResponse.builder()
                .authorId(u.getId())
                .nickname(u.getNickname())
                .accountNumber(u.getAccountNumber())
                .accountBank(u.getAccountBank())
                .profileImageUrl(u.getImageUrl())
                .build();
    }


    // 참여 공구 리스트 조회
    public ParticipatedListResponse toParticipatedListResponse(Order o) {
        GroupBuy post = o.getGroupBuy();

        String img = post.getImages().stream()
                .findFirst()
                .map(Image::getImageKey)
                .orElse(null);

        boolean dueSoon = "OPEN".equals(post.getPostStatus())
                && post.getDueDate().isAfter(LocalDateTime.now())
                && post.getDueDate().isBefore(LocalDateTime.now().plusDays(3));

        return ParticipatedListResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .postStatus(post.getPostStatus())
                .location(post.getLocation())
                .imageKey(img)
                .soldAmount(post.getTotalAmount() - post.getLeftAmount())
                .totalAmount(post.getTotalAmount())
                .participantCount(post.getParticipantCount())
                .dueSoon(dueSoon)
                .build();
    }
}
