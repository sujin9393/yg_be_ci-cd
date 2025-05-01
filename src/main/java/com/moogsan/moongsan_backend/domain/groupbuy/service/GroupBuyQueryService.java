package com.moogsan.moongsan_backend.domain.groupbuy.service;

import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.ImageResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyDetail.DetailResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyDetail.UserProfileResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.BasicList.BasicListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.PagedResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.ParticipantList.ParticipantListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyMain.MainPageResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyMain.MainSummaryResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyMain.SectionResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyUpdate.GroupBuyForUpdateResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.Image;
import com.moogsan.moongsan_backend.domain.groupbuy.repository.GroupBuyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class GroupBuyQueryService {
    private final GroupBuyRepository groupBuyRepository;
    // private final WishRepository wishRepository;

    /// 공구 게시글 수정 전 정보 조회
    public GroupBuyForUpdateResponse getGroupBuyEditInfo(Long postId) {
        GroupBuy groupBuy = groupBuyRepository.findWithImagesById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        List<ImageResponse> imageUrls = groupBuy.getImages().stream()
                .map(image -> ImageResponse.builder()
                        .imageUrl(image.getImageUrl())
                        .imageSeqNo(image.getImageSeqNo())
                        .thumbnail(image.isThumbnail())
                        .build())
                .toList();

        return GroupBuyForUpdateResponse.builder()
                .title(groupBuy.getTitle())
                .name(groupBuy.getName())
                .description(groupBuy.getDescription())
                .url(groupBuy.getUrl())
                .imageUrls(imageUrls)
                .dueDate(groupBuy.getDueDate())
                .location(groupBuy.getLocation())
                .pickupDate(groupBuy.getPickupDate())
                .price(groupBuy.getPrice())
                .unitAmount(groupBuy.getUnitAmount())
                .totalAmount(groupBuy.getTotalAmount())
                .build();
    }

    /// 메인 페이지 조회 -> 얘는 과연 분리하는게 좋은가 합치는게 좋은가(프론트)?
    public MainPageResponse getGroupBuyMainInfo() {

        // 섹션 별 조회
        List<GroupBuy> endingSoon = groupBuyRepository.findWithImagesByIsSoonTrueOrderByCreatedAtAsc(
                        PageRequest.of(0, 10, Sort.by("createdAt").ascending()))
                .orElseThrow(() -> new IllegalArgumentException("결과가 없습니다."));

        List<GroupBuy> latest = groupBuyRepository.findWithImages(
                        PageRequest.of(0, 10, Sort.by("createdAt").ascending()))
                .orElseThrow(() -> new IllegalArgumentException("결과가 없습니다."));

        List<GroupBuy> moongsanPick = groupBuyRepository.findWithImagesByIsSoonTrueOrderByCreatedAtAsc(
                        PageRequest.of(0, 10, Sort.by("createdAt").ascending()))
                .orElseThrow(() -> new IllegalArgumentException("결과가 없습니다."));

        // Wish logic commented out
        // Set<Long> endingSoonWish = fetchWishIds(userId, endingSoon);
        // Set<Long> latestWish     = fetchWishIds(userId, latest);
        // Set<Long> pickWish       = fetchWishIds(userId, moongsanPick);

        // 섹션별 DTO
        SectionResponse endingSoonSection = toSection("endingSoon", "마감 임박!!", endingSoon);
        SectionResponse latestSection     = toSection("latest",     "전체",     latest);
        SectionResponse pickSection       = toSection("moongsanPick","뭉산 PICK", moongsanPick);

        return MainPageResponse.builder()
                .boards(List.of(endingSoonSection, latestSection, pickSection))
                .build();
    }

    // (일반 메소드) 메인 페이지 리스트 내 섹션 변환
    private SectionResponse toSection(String code,
                                      String displayName,
                                      List<GroupBuy> posts) {
        List<MainSummaryResponse> dtoList = posts.stream()
                .map(p -> MainSummaryResponse.builder()  // MainSummaryResponse 내부 of 함수로 넣기
                        .postId(p.getId())
                        .title(p.getTitle())
                        .name(p.getName())
                        .postStatus(p.getPostStatus())
                        .imageUrl(p.getImages().stream()
                                .findFirst()
                                .map(Image::getImageUrl)
                                .orElse(null))
                        .unitPrice(p.getUnitPrice())
                        .unitAmount(p.getUnitAmount())
                        .soldAmount(p.getTotalAmount() - p.getLeftAmount())
                        .totalAmount(p.getTotalAmount())
                        //.isWish(wishIds.contains(p.getId()))
                        .build()
                )
                .collect(Collectors.toList());

        return SectionResponse.builder()
                .code(code)
                .displayName(displayName)
                .posts(dtoList)
                .build();
    }

    // Wish-related methods commented out
    /*
    // (일반 메소드) 리스트 내 위시 여부 조회
    private Set<Long> fetchWishIds(Long userId, List<GroupBuy> posts) {
        if (userId == null || posts.isEmpty()) {
            return Collections.emptySet();
        }
        List<Long> ids = posts.stream().map(GroupBuy::getId).toList();
        return wishRepository.findPostIdsByUserAndPostIds(userId, ids);
    }
    */

    /// 공구 리스트 조회
    public PagedResponse getGroupBuyBasicList(Long categoryId, String sort, Long cursor, Integer limit) {
        Sort jpaSort = switch (sort) {
            case "price_asc" -> Sort.by("unitPrice").ascending();
            case "ending_soon" -> Sort.by("dueDate").ascending();
            default -> Sort.by("createdAt").descending();
        };

        Long cursorId = (cursor != null) ? cursor : Long.MAX_VALUE;
        Pageable page = PageRequest.of(0, limit, jpaSort);

        List<GroupBuy> entities;
        if (categoryId != null) {
            entities = groupBuyRepository.findByCategoryIdAndIdLessThan(categoryId, cursorId, page);
        } else {
            entities = groupBuyRepository.findByIdLessThan(cursorId, page);
        }

        List<BasicListResponse> posts = entities.stream()
                .map(g -> {
                    List<ImageResponse> imageResponses = g.getImages().stream()
                            .map(img -> ImageResponse.builder()
                                    .imageUrl(img.getImageUrl())
                                    .imageSeqNo(img.getImageSeqNo())
                                    .thumbnail(img.isThumbnail())
                                    .build()
                            )
                            .collect(Collectors.toList());

                    String thumbnailUrl = imageResponses.stream()
                            .filter(ImageResponse::isThumbnail)
                            .findFirst()
                            .map(ImageResponse::getImageUrl)
                            .orElse(imageResponses.isEmpty() ? null : imageResponses.getFirst().getImageUrl());

                    return BasicListResponse.builder()
                            .postId(g.getId())
                            .title(g.getTitle())
                            .name(g.getName())
                            .postStatus(g.getPostStatus())
                            .imageUrls(imageResponses)
                            .unitPrice(g.getUnitPrice())
                            .unitAmount(g.getUnitAmount())
                            .soldAmount(g.getTotalAmount() - g.getLeftAmount())
                            .totalAmount(g.getTotalAmount())
                            .participantCount(g.getParticipantCount())
                            .dueSoon(g.isDueSoon())
                            //.isWish()
                            .createdAt(g.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        Integer nextCursor = posts.isEmpty() ? null : Math.toIntExact(posts.getLast().getPostId());
        boolean hasMore = entities.size() == limit;

        return PagedResponse.<BasicListResponse>builder()
                .count(posts.size())
                .posts(posts)
                .nextCursor(nextCursor)
                .hasMore(hasMore)
                .build();
    }

    /// 공구 게시글 상세 조회
    public DetailResponse getGroupBuyDetailInfo(Long postId) {
        GroupBuy groupBuy = groupBuyRepository.findWithImagesById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        List<ImageResponse> imageUrls = groupBuy.getImages().stream()
                .map(image -> ImageResponse.builder()
                        .imageUrl(image.getImageUrl())
                        .imageSeqNo(image.getImageSeqNo())
                        .thumbnail(image.isThumbnail())
                        .build())
                .toList();

        return DetailResponse.builder()
                .postId(groupBuy.getId())
                .title(groupBuy.getTitle())
                .name(groupBuy.getName())
                .postStatus(groupBuy.getPostStatus())
                .description(groupBuy.getDescription())
                .url(groupBuy.getUrl())
                .imageUrls(imageUrls)
                .unitPrice(groupBuy.getUnitPrice())
                .unitAmount(groupBuy.getUnitAmount())
                .soldAmount(groupBuy.getTotalAmount() - groupBuy.getLeftAmount())
                .totalAmount(groupBuy.getTotalAmount())
                .participantCount(groupBuy.getParticipantCount())
                .createdAt(groupBuy.getCreatedAt())
                .userProfileResponse(
                        UserProfileResponse.builder()
                                .authorId(groupBuy.getUser().getId())
                                .nickName(groupBuy.getUser().getNickname())
                                .accountNumber(groupBuy.getUser().getAccountNumber())
                                .profileImageUrl(groupBuy.getUser().getImageUrl())
                                .build()
                )
                .build();
    }

    /// 관심 공구 리스트 조회
    public PagedResponse getGroupBuyWishList(Long cursor, Integer limit) {
        // TODO V2
        return null;
    }

    /// 주최 공구 리스트 조회
    public PagedResponse getGroupBuyHostedList(String sort, Long cursor, Integer limit) {
        // TODO V2
        return null;
    }

    /// 참여 공구 리스트 조회
    /* Order 완료 후 작업 이어서 진행
    public PagedResponse<ParticipatedListResponse> getGroupBuyParticipatedList(String sort, Long cursor, Integer limit) {
        Long userId = SecurityUtil.getCurrentUserId();
        Long cursorId = Optional.ofNullable(cursor).orElse(Long.MAX_VALUE);

        // 페이징 & 정렬 (post.id 내림차순)
        Pageable page = PageRequest.of(0, limit, Sort.by("post.id").descending());

        // 주문(Participation) 조회
        //List<Order> orders = orderRepository
        //        .findByUserIdAndPost_PostStatusAndPostIdLessThan(userId, statusFilter, cursorId, page);

        // DTO 매핑
        List<ParticipatedListResponse> posts = orders.stream()
                .map(o -> {
                    GroupBuy post = o.getPost();
                    String img = post.getImages().stream()
                            .findFirst()
                            .map(Image::getImageUrl)
                            .orElse(null);

                    // 마감 임박 여부 (OPEN 중이고 dueDate까지 3일 이내)
                    boolean dueSoon = Objects.equals(post.getPostStatus(), "OPEN") &&
                            post.getDueDate().isAfter(LocalDateTime.now()) &&
                            post.getDueDate().isBefore(LocalDateTime.now().plusDays(3));

                    // 찜 여부 확인
                    //boolean isWish = wishRepository.existsByUserIdAndPostId(userId, post.getId());

                    return ParticipatedListResponse.builder()
                            // 식별/메타
                            .postId(post.getId())
                            .title(post.getTitle())
                            .postStatus(post.getPostStatus())

                            // 본문
                            .location(post.getLocation())
                            .imageUrl(img)

                            // 숫자 데이터
                            //.price(o.getPrice())
                            //.orderQuantity(o.getQuantity())
                            .soldAmount(post.getTotalAmount() - post.getLeftAmount())
                            .totalAmount(post.getTotalAmount())
                            .participantCount(post.getParticipantCount())

                            // 상태/플래그
                            //.orderStatus(o.getOrderStatus().name())
                            .dueSoon(dueSoon)
                            //.isWish(isWish)
                            .build();
                })
                .toList();

        // 다음 커서 및 더보기 여부
        Integer nextCursor = Math.toIntExact(posts.isEmpty()
                ? null
                : posts.getLast().getPostId());
        boolean hasMore = posts.size() == limit;

        return PagedResponse.<ParticipatedListResponse>builder()
                .count(posts.size())
                .posts(posts)
                .nextCursor(nextCursor)
                .hasMore(hasMore)
                .build();
    }
     */

    /// 공구 참여자 조회
    public ParticipantListResponse getGroupBuyParticipantsInfo(Long postId) {
        // TODO V2
        return null;
    }
}