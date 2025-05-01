package com.moogsan.moongsan_backend.domain.user.controller;

import com.moogsan.moongsan_backend.domain.user.dto.request.SignUpRequest;
import com.moogsan.moongsan_backend.domain.user.dto.response.LoginResponse;
import com.moogsan.moongsan_backend.domain.user.service.SignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User 관련 API 요청을 처리하는 Controller 클래스.
 * 회원가입 및 자동 로그인 기능을 담당한다.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService; // 사용자 관련 비즈니스 로직을 처리하는 서비스 의존성

    /**
     * 회원가입 요청을 처리하고, 회원가입 완료 후 자동 로그인을 진행한다.
     *
     * @param request 회원가입 요청 데이터 (유효성 검증 대상)
     * @return 로그인 응답 데이터 (LoginResponse)
     */
    @PostMapping
    public ResponseEntity<LoginResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        LoginResponse response = signUpService.signUp(request);
        return ResponseEntity.ok(response);
    }
}