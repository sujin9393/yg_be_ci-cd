package com.moogsan.moongsan_backend.domain.groupbuy.service;

import com.moogsan.moongsan_backend.domain.groupbuy.dto.command.request.CreateGroupBuyRequest;
import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import com.moogsan.moongsan_backend.domain.groupbuy.repository.GroupBuyRepository;
import com.moogsan.moongsan_backend.domain.user.entity.CustomUserDetails;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupBuyCommandService {

    private final GroupBuyRepository groupBuyRepository;
    private final Path uploadDir;         // 업로드 디렉토리(application.yml에 설정 필요)
    private final String publicBaseUrl;   // 외부에 공개되는 베이스 URL (https://cdn.example.com/uploads 등등)
    //private final OrderRepository orderRepository;

    /// 공구 게시글 작성
    public Long createGroupBuy(User currentUser, CreateGroupBuyRequest createGroupBuyRequest) {

        // GroupBuy 기본 필드 매핑 (팩토리 메서드 사용)
        GroupBuy gb = GroupBuy.of(createGroupBuyRequest, currentUser);

        // 최종 저장
        return groupBuyRepository.save(gb).getId();
    }

    /// 공구 게시글 수정
    // TODO V2

    /// 공구 게시글 삭제
    // TODO V2

    /*
    /// 공구 참여 취소
    public void leaveGroupBuy(User currentUser, Long postId) {

        // 해당 공구가 존재하는지 조회 -> 없으면 404
        GroupBuy groupBuy = groupBuyRepository.findById(postId)
                .orElseThrow(GroupBuyNotFoundException::new);

        // 해당 공구가 OPEN인지 조회, dueDate가 현재 이후인지 조회 -> 아니면 409
        if (groupBuy.getPostStatus().equals("OPEN")
            || groupBuy.getDueDate().isBefore(LocalDateTime.now())) {
            throw new GroupBuyInvalidStateException("공구 참여 취소는 공구가 열려있는 상태에서만 가능합니다.");
        }

        // 해당 공구의 주문 테이블에 해당 유저의 주문이 존재하는지 조회 -> 아니면 404
        Order order = orderRepository.findByUserId(currentUser.getId())
                .orElseThrow(OrderNotFoundException::new);

        // 해당 주문의 상태가 canceled가 아닌지 조회 -> 아니면 409
        if (order.getOrderStatus().equals("CANCELED")) {
            throw new OrderInvalidStateException("이미 취소된 주문입니다.");
        }

        // 해당 주문의 상태가 paid인지 조회
        if (order.getOrderStatus().equals("PAID")) {
            // 별도의 환불 로직 처리 필요
        }

        // 남은 수량, 참여 인원 수 업데이트
        int returnQuantity = order.getQuantity();
        groupBuy.setLeftAmount(groupBuy.getLeftAmount() + returnQuantity);
        groupBuy.setParticipantCount(groupBuy.getParticipantCount() - 1);

        // 해당 유저의 주문을 취소
        order.setOrderStatus("CANCELED");

    }
     */

    /// 관심 공구 추가
    // TODO V2

    /// 관심 공구 취소
    // TODO V2

    /*
    /// 공구 게시글 공구 종료
    public void endGroupBuy(User currentUser, Long postId) {

        // 해당 공구가 존재하는지 조회 -> 없으면 404
        GroupBuy groupBuy = groupBuyRepository.findById(postId)
                .orElseThrow(GroupBuyNotFoundException::new);

        // 해당 공구가 ENDED가 아닌지 조회 -> 아니면 409
        if (!groupBuy.getPostStatus().equals("ENDED")) {
            throw new GroupBuyInvalidStateException("공구 종료는 모집 마감 이후에만 가능합니다.");
        }

        // dueDate와 pickupDate 이후인지 조회 -> 아니면 409
        if (groupBuy.getDueDate().isBefore(LocalDateTime.now())) {
            throw new GroupBuyInvalidStateException("공구 종료는 공구 마감 일자 이후에만 가능합니다.");
        }

        if (groupBuy.getDueDate().isBefore(LocalDateTime.now())) {
            throw new GroupBuyInvalidStateException("공구 종료는 공구 픽업 일자 이후에만 가능합니다.");
        }

        // 해당 공구의 작성자가 해당 유저인지 조회 -> 아니면 403
        if(!groupBuy.getUser().getId().equals(currentUser.getId())) {
            throw new GroupBuyNotHostException();
        }

        //공구 게시글 status ENDED로 변경
        groupBuy.setPostStatus("ENDED");

        // TODO V2, V3에서는 참여자 채팅방 해제, 익명 채팅방 즉시 해제

    }
     */

    /// 검색
    // TODO V2
}
