package com.moogsan.moongsan_backend.domain.user.service;

import com.moogsan.moongsan_backend.domain.user.dto.request.SignUpRequest;
import com.moogsan.moongsan_backend.domain.user.dto.response.LoginResponse;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import com.moogsan.moongsan_backend.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 비즈니스 로직을 담당하는 서비스 계층 컴포넌트
@RequiredArgsConstructor // final 필드를 매개변수로 받는 생성자 자동 생성
public class SignUpService {

    private final UserRepository userRepository; // 사용자 데이터베이스 접근 객체
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화기
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponse signUp(SignUpRequest request) {
        validateDuplicateUser(request);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .accountBank(request.getAccountBank())
                .accountNumber(request.getAccountNumber())
                .imageUrl(request.getImageUrl())
                .type("USER")
                .status("ACTIVE")
                .joinedAt(java.time.LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        String accessToken = jwtUtil.generateAccessToken(savedUser);
        String refreshToken = jwtUtil.generateRefreshToken(savedUser);
        Long accessTokenExpireAt = jwtUtil.getAccessTokenExpireAt();

        return new LoginResponse(
                savedUser.getId(),
                savedUser.getNickname(),
                accessToken,
                refreshToken,
                accessTokenExpireAt,
                "https://example.com/home"
        );
    }

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