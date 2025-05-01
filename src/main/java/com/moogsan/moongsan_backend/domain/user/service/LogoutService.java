package com.moogsan.moongsan_backend.domain.user.service;

import com.moogsan.moongsan_backend.domain.user.entity.User;
import com.moogsan.moongsan_backend.domain.user.repository.TokenRepository;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final UserRepository userRepository;
    private final TokenRepository refreshTokenRepository;

    @Transactional
    public void logout(Long userId) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 2. 로그아웃 시간 기록
        user.setLogoutAt(LocalDateTime.now());

        // 3. 리프레시 토큰 삭제
        refreshTokenRepository.deleteByUserId(userId);
    }
}