package com.moogsan.moongsan_backend.domain.order.repository;

import com.moogsan.moongsan_backend.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 유저 ID로 주문 목록 조회
    List<Order> findByUserId(Long userId);

    // 공구글 ID로 주문 목록 조회
    List<Order> findByGroupBuyId(Long groupBuyId);

    // 유저 ID + 공구글 ID로 주문 단건 조회
    Optional<Order> findByUserIdAndGroupBuyId(Long userId, Long groupBuyId);
}
