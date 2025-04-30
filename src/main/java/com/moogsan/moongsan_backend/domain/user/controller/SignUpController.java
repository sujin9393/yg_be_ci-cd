package com.moogsan.moongsan_backend.domain.user.signup.controller;

import com.moogsan.moongsan_backend.domain.user.signup.dto.SignUpRequest;
import com.moogsan.moongsan_backend.domain.user.signup.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * User 관련 API 요청을 처리하는 Controller 클래스.
 * 회원가입 기능을 담당한다.
 */
@RestController // HTTP 요청을 처리하고 JSON 형태로 응답을 반환하는 컨트롤러
@RequestMapping("/api/users") // 이 컨트롤러 내 모든 엔드포인트의 공통 URL 경로
@RequiredArgsConstructor // final 필드를 매개변수로 받는 생성자를 Lombok이 자동 생성
public class SignUpController {

    private final SignUpService signUpService; // 사용자 관련 비즈니스 로직을 처리하는 서비스 의존성

    /**
     * 회원가입 요청을 처리하는 메서드.
     *
     * @param request 회원가입 요청 데이터 (유효성 검증 대상)
     * @return 회원가입 성공 시 201 Created 응답 반환
     */
    @PostMapping
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        signUpService.signUp(request); // 회원가입 비즈니스 로직 위임
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created 상태 반환
    }
}