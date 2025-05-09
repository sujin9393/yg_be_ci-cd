package com.moogsan.moongsan_backend.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.moogsan.moongsan_backend.domain.order.dto.request.OrderCreateRequest;
import com.moogsan.moongsan_backend.domain.order.service.OrderCreateService;
import com.moogsan.moongsan_backend.domain.order.service.OrderCancelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.LinkedHashMap;
import com.moogsan.moongsan_backend.domain.user.entity.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCreateService orderCreateService;
    private final OrderCancelService orderCancelService;

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderCreateRequest orderCreateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        orderCreateService.createOrder(orderCreateRequest, Long.valueOf(String.valueOf(userDetails.getUser().getId())));
//        orderCreateService.changePostStatus(orderCreateRequest.getPostId()); // 남은 수량 0일시 공구 종료
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "주문이 성공적으로 등록되었습니다.");
        response.put("data", null);
        return ResponseEntity.status(201).body(response);
    }

    // 주문 취소는 공구 상세 페이지에서 진행
//    @DeleteMapping("/orders/postId/{postId}")
//    public ResponseEntity<?> cancelOrder(
//            @PathVariable Long postId,
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        orderCancelService.cancelOrder(userDetails.getUser().getId(), postId);
//        Map<String, Object> response = new LinkedHashMap<>();
//        response.put("message", "주문이 성공적으로 취소되었습니다.");
//        response.put("data", null);
//        return ResponseEntity.ok().body(response);
//    }
}
