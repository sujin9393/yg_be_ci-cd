package com.moogsan.moongsan_backend.domain.user.signup.service;

import com.moogsan.moongsan_backend.domain.user.signup.dto.SignUpRequest;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스.
 * 회원가입 기능을 제공한다.
 */
@Service // 비즈니스 로직을 담당하는 서비스 계층 컴포넌트
@RequiredArgsConstructor // final 필드를 매개변수로 받는 생성자 자동 생성
public class SignUpService {

    private final UserRepository userRepository; // 사용자 데이터베이스 접근 객체
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화기

    /**
     * 회원가입 요청을 처리하는 메서드.
     * - 중복 검사
     * - 비밀번호 암호화
     * - User 엔티티 생성 및 저장
     *
     * @param request 회원가입 요청 DTO
     * @return 저장된 User 엔티티
     */
    public User signUp(SignUpRequest request) {
        validateDuplicateUser(request); // 이메일, 닉네임, 전화번호 중복 검사

        // 요청 데이터를 바탕으로 User 엔티티 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .nickname(request.getNickname())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .accountBank(request.getAccountBank())
                .accountNumber(request.getAccountNumber())
                .imageUrl(request.getImageUrl()) // 가입 시 이미지 설정
                .type("USER") // 기본 사용자 타입 설정
                .status("ACTIVE") // 기본 상태 설정
                .joinedAt(java.time.LocalDateTime.now()) // 가입 시각 저장
                .build();

        // 사용자 정보 저장 및 반환
        return userRepository.save(user);
    }

    /**
     * 회원가입 시 이메일, 닉네임, 전화번호의 중복 여부를 검증하는 메서드.
     * 중복이 발견되면 IllegalArgumentException을 던진다.
     *
     * @param request 회원가입 요청 DTO
     */
    private void validateDuplicateUser(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 등록된 닉네임입니다.");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
        }
    }
}