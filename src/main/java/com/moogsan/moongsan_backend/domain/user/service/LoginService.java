package com.moogsan.moongsan_backend.domain.user.service;

import com.moogsan.moongsan_backend.domain.user.dto.request.LoginRequest;
import com.moogsan.moongsan_backend.domain.user.dto.response.LoginResponse;
import com.moogsan.moongsan_backend.domain.user.entity.Token;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import com.moogsan.moongsan_backend.domain.user.repository.TokenRepository;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import com.moogsan.moongsan_backend.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자가 존재하지 않습니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 추가. 탈퇴 복구 처리
        if (user.getDeletedAt() != null) {
            user.setDeletedAt(null);
        }

        // 3. JWT 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        Long accessTokenExpireAt = jwtUtil.getAccessTokenExpireAt(); // 만료 시간 (millis)
        Long refreshTokenExpireMillis = jwtUtil.getRefreshTokenExpireMillis(); // 리프레시 토큰 만료 기간 (millis)

        // 4. 기존 리프레시 토큰 제거 (동일 유저 기준)
        refreshTokenRepository.deleteByUserId(user.getId());

        // 5. 새로운 리프레시 토큰 저장
        Token newToken = new Token(
                null, // ID는 auto increment
                user.getId(),
                refreshToken,
                LocalDateTime.now().plusSeconds(refreshTokenExpireMillis / 1000)
        );
        refreshTokenRepository.save(newToken);

        // 6. 응답 반환
        return new LoginResponse(
                user.getId(),
                user.getNickname(),
                accessToken,
                refreshToken,
                accessTokenExpireAt,
                "https://example.com/home" // 홈페이지로 redirect
        );
    }
}