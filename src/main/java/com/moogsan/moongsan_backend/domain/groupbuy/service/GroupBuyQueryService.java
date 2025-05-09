package com.moogsan.moongsan_backend.domain.groupbuy.service;

import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyDetail.DetailResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.BasicList.BasicListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.HostedList.HostedListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.PagedResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.ParticipantList.ParticipantListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.ParticipantList.ParticipantResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyList.ParticipatedList.ParticipatedListResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.dto.query.response.groupBuyUpdate.GroupBuyForUpdateResponse;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import com.moogsan.moongsan_backend.domain.groupbuy.exception.specific.GroupBuyNotFoundException;
import com.moogsan.moongsan_backend.domain.groupbuy.mapper.GroupBuyQueryMapper;
import com.moogsan.moongsan_backend.domain.groupbuy.repository.GroupBuyRepository;
import com.moogsan.moongsan_backend.domain.order.entity.Order;
import com.moogsan.moongsan_backend.domain.order.repository.OrderRepository;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class GroupBuyQueryService {
    private final GroupBuyRepository groupBuyRepository;
    private final OrderRepository orderRepository;
    private final GroupBuyQueryMapper groupBuyQueryMapper;

    /// 공구 게시글 수정 전 정보 조회
    /// TODO V2
    public GroupBuyForUpdateResponse getGroupBuyEditInfo(Long postId) {
        GroupBuy groupBuy = groupBuyRepository.findWithImagesById(postId)
                .orElseThrow(GroupBuyNotFoundException::new);

        return groupBuyQueryMapper.toUpdateResponse(groupBuy);
    }

    /// TODO 모든 조회 로직에 위시 여부 조회 추가(리턴되는 리스트 위시 여부만 조회)
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

    /// 검색
    // TODO V2 -> 메서드명 고민해보기
    public PagedResponse getSearchList(Long cursor, Integer limit) {

        return null;
    }


    /// 공구 리스트 조회
    public PagedResponse<BasicListResponse> getGroupBuyListByCursor(
            Long categoryId,
            String sort,
            Long cursorId,
            LocalDateTime cursorCreatedAt,
            Integer cursorPrice,
            Integer limit
    ) {
        // 페이징 객체 생성 (첫 페이지는 0번 인덱스, limit 만큼)
        Pageable page = PageRequest.of(0, limit);

        // 각 정렬에 따라 cursor 유무 분기
        List<GroupBuy> entities;
        switch (sort) {
            case "price_asc":
                int lastPrice   = (cursorPrice != null)   ? cursorPrice     : 0;
                LocalDateTime lastCreated = (cursorCreatedAt != null)
                        ? cursorCreatedAt
                        : LocalDateTime.now();

                if (cursorId == null) {
                    // --- 첫 페이지: 단순 가격 오름차순 ---
                    if (categoryId != null) {
                        entities = groupBuyRepository.findByCategoryPriceOrder(categoryId, page);
                    } else {
                        entities = groupBuyRepository.findAllByPriceOrder(page);
                    }
                } else {
                    // --- 다음 페이지: unitPrice ASC + createdAt DESC + id DESC cursor ---
                    if (categoryId != null) {
                        entities = groupBuyRepository.findByCategoryAndPriceAscCursor(
                                categoryId, lastPrice, lastCreated, cursorId, page);
                    } else {
                        entities = groupBuyRepository.findByPriceAscCursor(
                                lastPrice, lastCreated, cursorId, page);
                    }
                }
                break;

            case "ending_soon":
                LocalDateTime lastDueCreated = (cursorCreatedAt != null)
                        ? cursorCreatedAt
                        : LocalDateTime.now();

                if (cursorId == null) {
                    // --- 첫 페이지: 마감 임박순 ---
                    if (categoryId != null) {
                        entities = groupBuyRepository.findByCategoryDueSoonOrder(categoryId, page);
                    } else {
                        entities = groupBuyRepository.findAllByDueSoonOrder(page);
                    }
                } else {
                    // --- 다음 페이지: dueSoon cursor (createdAt DESC, id DESC) ---
                    if (categoryId != null) {
                        entities = groupBuyRepository.findByCategoryAndDueSoonCursor(
                                categoryId, lastDueCreated, cursorId, page);
                    } else {
                        entities = groupBuyRepository.findByDueSoonCursor(
                                lastDueCreated, cursorId, page);
                    }
                }
                break;

            default:  // 최신순 (createdAt DESC, id DESC)
                if (cursorId == null) {
                    // --- 첫 페이지: 최신순 ---
                    if (categoryId != null) {
                        entities = groupBuyRepository.findByCategoryCreatedOrder(categoryId, page);
                    } else {
                        entities = groupBuyRepository.findAllByCreatedOrder(page);
                    }
                } else {
                    // --- 다음 페이지: created cursor ---
                    if (categoryId != null) {
                        entities = groupBuyRepository.findByCategoryAndCreatedCursor(
                                categoryId, cursorId, page);
                    } else {
                        entities = groupBuyRepository.findByCreatedCursor(cursorId, page);
                    }
                }
                break;
        }

        // DTO 매핑
        List<BasicListResponse> posts = entities.stream()
                .map(groupBuyQueryMapper::toBasicListResponse)
                .collect(Collectors.toList());

        // 다음 커서 & hasMore 계산
        boolean hasMore = posts.size() == limit;

        Long nextCursor      = null;
        Integer nextCursorPrice = null;
        LocalDateTime nextCreatedAt = null;

        if (hasMore) {
            BasicListResponse last = posts.getLast();
            nextCursor    = last.getPostId();
            nextCreatedAt = last.getCreatedAt();
            if ("price_asc".equals(sort)) {
                nextCursorPrice = last.getUnitPrice();
            }
        }
        
        return PagedResponse.<BasicListResponse>builder()
                .count(posts.size())
                .posts(posts)
                .nextCursor(nextCursor != null ? nextCursor.intValue() : null)  // int로 변환
                .nextCursorPrice(nextCursorPrice)
                .nextCreatedAt(nextCreatedAt)
                .hasMore(hasMore)
                .build();
    }

    /// 공구 게시글 상세 조회
    public DetailResponse getGroupBuyDetailInfo(Long userId, Long postId) {

        GroupBuy groupBuy = groupBuyRepository.findWithImagesById(postId)
                .orElseThrow(GroupBuyNotFoundException::new);

        boolean isParticipant = orderRepository.existsByUserIdAndGroupBuyId(userId, postId);

        return groupBuyQueryMapper.toDetailResponse(groupBuy, isParticipant);
    }

    /// 관심 공구 리스트 조회
    /// TODO V2
    /*
    public PagedResponse<WishListResponse> getGroupBuyWishList(
            User currentUser,
            String sort,
            Long cursorId,
            Integer limit) {
        String status = sort.toUpperCase();

        Pageable page = PageRequest.of(0, limit, Sort.by("groupBuy.id").descending());

        // cursorId가 없으면 cursor 조건 제외
        List<GroupBuy> groupBuys;
        if (cursorId == null) {
            groupBuys = wishRepository.findByUserIdAndGroupBuyPostStatus(
                    currentUser.getId(),
                    status,
                    page
            );
        } else {
            groupBuys = wishRepository.findByUserIdAndGroupBuyPostStatusAndGroupBuyIdLessThan(
                    currentUser.getId(),
                    status,
                    cursorId,
                    page
            );
        }

        // 매핑
        List<WishListResponse> posts = groupBuys.stream()
                .map(groupBuyQueryMapper::toWishListResponse)
                .toList();

        // 다음 커서 및 더보기 여부
        Long nextCursor = posts.isEmpty()
                ? null
                : posts.getLast().getPostId();
        boolean hasMore = posts.size() == limit;

        return PagedResponse.<WishListResponse>builder()
                .count(posts.size())
                .posts(posts)
                .nextCursor(nextCursor != null ? nextCursor.intValue() : null)
                .hasMore(hasMore)
                .build();
    }

     */


    /// 주최 공구 리스트 조회
    public PagedResponse<HostedListResponse> getGroupBuyHostedList(
            User currentUser,
            String sort,
            Long cursorId,
            Integer limit) {

        String status = sort.toUpperCase();

        Pageable page = PageRequest.of(0, limit, Sort.by("groupBuy.id").descending());

        // cursorId가 없으면 cursor 조건 제외
        List<GroupBuy> groupBuys;
        if (cursorId == null) {
            groupBuys = groupBuyRepository.findByUser_IdAndPostStatus (
                    currentUser.getId(),
                    status,
                    page
            );
        } else {
            groupBuys = groupBuyRepository.findByUser_IdAndPostStatusAndIdLessThan (
                    currentUser.getId(),
                    status,
                    cursorId,
                    page
            );
        }

        // 매핑
        List<HostedListResponse> posts = groupBuys.stream()
                .map(groupBuyQueryMapper::toHostedListResponse)
                .toList();

        // 다음 커서 및 더보기 여부
        Long nextCursor = posts.isEmpty()
                ? null
                : posts.getLast().getPostId();
        boolean hasMore = posts.size() == limit;

        return PagedResponse.<HostedListResponse>builder()
                .count(posts.size())
                .posts(posts)
                .nextCursor(nextCursor != null ? nextCursor.intValue() : null)
                .hasMore(hasMore)
                .build();
    }


    /// 참여 공구 리스트 조회
    public PagedResponse<ParticipatedListResponse> getGroupBuyParticipatedList(
            User currentUser,
            String sort,
            Long cursorId,
            Integer limit
    ) {
        String status = sort.toUpperCase();

        Pageable page = PageRequest.of(0, limit, Sort.by("groupBuy.id").descending());

        // cursorId가 없으면 cursor 조건 제외
        List<Order> orders;
        if (cursorId == null) {
            orders = orderRepository.findByUserIdAndGroupBuy_PostStatus(
                    currentUser.getId(),
                    status,
                    page
            );
        } else {
            orders = orderRepository.findByUserIdAndGroupBuy_PostStatusAndIdLessThan(
                    currentUser.getId(),
                    status,
                    cursorId,
                    page
            );
        }

        // 매핑
        List<ParticipatedListResponse> posts = orders.stream()
                .map(groupBuyQueryMapper::toParticipatedListResponse)
                .toList();

        // 다음 커서 및 더보기 여부
        Long nextCursor = posts.isEmpty()
                ? null
                : posts.getLast().getPostId();
        boolean hasMore = posts.size() == limit;

        return PagedResponse.<ParticipatedListResponse>builder()
                .count(posts.size())
                .posts(posts)
                .nextCursor(nextCursor != null ? nextCursor.intValue() : null)
                .hasMore(hasMore)
                .build();
    }

    /// 공구 참여자 조회
    public ParticipantListResponse getGroupBuyParticipantsInfo(Long postId) {
        List<Order> orders = orderRepository.findByGroupBuyIdAndStatusNot(postId, "canceled");

        List<ParticipantResponse> participantList = orders.stream()
                .map(groupBuyQueryMapper::toParticipantResponse)
                .toList();

        return ParticipantListResponse.builder()
                .participants(participantList)
                .build();
    }

}