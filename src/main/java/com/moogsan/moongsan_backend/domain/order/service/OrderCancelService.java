package com.moogsan.moongsan_backend.domain.order.service;

import com.moogsan.moongsan_backend.domain.order.entity.Order;
import com.moogsan.moongsan_backend.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class OrderCancelService {

    private final OrderRepository orderRepository;

    @Transactional
    public void cancelOrder(Long userId, Long postId) {
        Order order = orderRepository.findByUserIdAndGroupBuyIdAndStatusNot(userId, postId, "CANCELED")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 내역을 찾을 수 없습니다."));
        if ("CANCELED".equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 취소된 주문입니다.");
        }
        order.cancel();

        // GroupBuy에 반영
        order.getGroupBuy().increaseLeftAmount(order.getQuantity());
        order.getGroupBuy().decreaseParticipantCount();
        orderRepository.save(order);
    }
}