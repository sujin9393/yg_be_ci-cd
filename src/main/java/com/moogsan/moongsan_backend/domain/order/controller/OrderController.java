package com.moogsan.moongsan_backend.domain.order.controller;

import com.moogsan.moongsan_backend.domain.order.dto.response.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.moogsan.moongsan_backend.domain.order.dto.request.OrderCreateRequest;
import com.moogsan.moongsan_backend.domain.order.service.OrderCreateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderCreateRequest orderCreateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        OrderCreateResponse responseData = orderCreateService.createOrder(orderCreateRequest, userDetails.getUser().getId());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "주문이 성공적으로 등록되었습니다.");
        response.put("data", responseData);
        return ResponseEntity.status(201).body(response);
    }
}
