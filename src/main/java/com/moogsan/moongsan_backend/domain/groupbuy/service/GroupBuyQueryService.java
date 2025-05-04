package com.moogsan.moongsan_backend.domain.groupbuy.service;

import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.ImageResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyDetail.DetailResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyDetail.UserProfileResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.BasicList.BasicListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.PagedResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.ParticipantList.ParticipantListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.ParticipatedList.ParticipatedListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyUpdate.GroupBuyForUpdateResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.Image;
import com.moogsan.moongsan_backend.domain.groupbuy.mapper.GroupBuyQueryMapper;
import com.moogsan.moongsan_backend.domain.groupbuy.repository.GroupBuyRepository;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class GroupBuyQueryService {
    private final GroupBuyRepository groupBuyRepository;
    // private final WishRepository wishRepository;
    private final GroupBuyQueryMapper groupBuyQueryMapper;

    /// 공구 게시글 수정 전 정보 조회
    public GroupBuyForUpdateResponse getGroupBuyEditInfo(Long postId) {
        GroupBuy groupBuy = groupBuyRepository.findWithImagesById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return groupBuyQueryMapper.toUpdateResponse(groupBuy);
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

    /// 공구 리스트 조회 => nextCursorId 뿐만 아니라 nextCursorPrice, nextCreatedAt 을 만드는 게 좋을까?
    public PagedResponse<BasicListResponse> getGroupBuyListByCursor(
            Long categoryId,
            String sort,
            Long cursorId,
            LocalDateTime cursorCreatedAt,
            Integer cursorPrice,
            Integer limit
    ) {
        // 1) 커서 ID 및 페이지 설정
        Pageable page = PageRequest.of(0, limit);

        // 2) 정렬 타입에 따라 서로 다른 @Query 메소드 호출
        List<GroupBuy> entities;
        switch (sort) {
            case "price_asc":
                // 최초 요청일 땐 lastPrice, lastCreatedAt 기본값
                int priceCursor    = (cursorPrice != null)   ? cursorPrice     : 0;
                LocalDateTime crt  = (cursorCreatedAt != null) ? cursorCreatedAt : LocalDateTime.now();
                if (categoryId != null) {
                    entities = groupBuyRepository.findByCategoryAndPriceAscCursor(
                            categoryId, priceCursor, crt, cursorId, page
                    );
                } else {
                    entities = groupBuyRepository.findByPriceAscCursor(
                            priceCursor, crt, cursorId, page
                    );
                }
                break;

            case "ending_soon":
                LocalDateTime dueCursorAt = (cursorCreatedAt != null)
                        ? cursorCreatedAt
                        : LocalDateTime.now();
                if (categoryId != null) {
                    entities = groupBuyRepository.findByCategoryAndDueSoonCursor(
                            categoryId, dueCursorAt, cursorId, page
                    );
                } else {
                    entities = groupBuyRepository.findByDueSoonCursor(
                            dueCursorAt, cursorId, page
                    );
                }
                break;

            default:  // 최신순
                if (categoryId != null) {
                    entities = groupBuyRepository.findByCategoryAndCreatedCursor(
                            categoryId, cursorId, page
                    );
                } else {
                    entities = groupBuyRepository.findByCreatedCursor(
                            cursorId, page
                    );
                }
                break;
        }

        // 3) DTO 변환
        List<BasicListResponse> posts = entities.stream()
                .map(groupBuyQueryMapper::toBasicListResponse)
                .collect(Collectors.toList());

        // 4) 다음 커서 & 더보기 여부 계산
        boolean hasMore = posts.size() == limit;

        Integer nextCursor      = null;
        Integer nextCursorPrice = null;
        LocalDateTime nextCreatedAt = null;

        if (hasMore) {
            BasicListResponse last = posts.get(posts.size() - 1);
            nextCursor = Math.toIntExact(last.getPostId());
            nextCreatedAt = last.getCreatedAt();

            if ("price_asc".equals(sort)) {
                nextCursorPrice = last.getUnitPrice();
            }
        }

        // 5) 응답
        return PagedResponse.<BasicListResponse>builder()
                .count(posts.size())
                .posts(posts)
                .nextCursor(nextCursor)
                .nextCursorPrice(nextCursorPrice)
                .nextCreatedAt(nextCreatedAt)
                .hasMore(hasMore)
                .build();
    }

    /// 공구 게시글 상세 조회
    public DetailResponse getGroupBuyDetailInfo(Long postId) {
        GroupBuy groupBuy = groupBuyRepository.findWithImagesById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return groupBuyQueryMapper.toDetailResponse(groupBuy);
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

    /*
    /// 참여 공구 리스트 조회
    public PagedResponse<ParticipatedListResponse> getGroupBuyParticipatedList(
            User currentUser,
            String sort,
            Long cursor,
            Integer limit
    ) {
        long cursorId = Optional.ofNullable(cursor).orElse(Long.MAX_VALUE);
        Pageable page = PageRequest.of(0, limit, Sort.by("post.id").descending());

        // 주문(Participation) 조회
        List<Order> orders = orderRepository
                .findByUserIdAndPostStatusAndPostIdLessThan(
                        currentUser.getId(),
                        "OPEN",            // 필요에 따라 sort→status 필터로 변경
                        cursorId,
                        page
                );

        // 매핑
        List<ParticipatedListResponse> posts = orders.stream()
                .map(groupBuyQueryMapper::toParticipatedListResponse)
                .toList();

        // 다음 커서 및 더보기 여부
        Long nextCursor = posts.isEmpty()
                ? null
                : posts.get(posts.size() - 1).getPostId();
        boolean hasMore = posts.size() == limit;

        return PagedResponse.<ParticipatedListResponse>builder()
                .count(posts.size())
                .posts(posts)
                .nextCursor(nextCursor != null ? nextCursor.intValue() : null)
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