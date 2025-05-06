package com.moogsan.moongsan_backend.domain.user.service;

import com.moogsan.moongsan_backend.domain.user.entity.User;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import com.moogsan.moongsan_backend.domain.user.repository.TokenRepository;
import com.moogsan.moongsan_backend.domain.user.exception.base.UserException;
import com.moogsan.moongsan_backend.domain.user.exception.code.UserErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Transactional
    public void withdraw(Long userId, HttpServletResponse response) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));

        user.setDeletedAt(LocalDateTime.now());

        tokenRepository.deleteByUserId(userId); // 리프레시 토큰 삭제

        jakarta.servlet.http.Cookie accessTokenCookie = new jakarta.servlet.http.Cookie("AccessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        response.addCookie(accessTokenCookie);
        response.addHeader("Set-Cookie", "AccessToken=; Max-Age=0; Path=/; HttpOnly; Secure; SameSite=None");

        jakarta.servlet.http.Cookie refreshTokenCookie = new jakarta.servlet.http.Cookie("RefreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);
        response.addHeader("Set-Cookie", "RefreshToken=; Max-Age=0; Path=/; HttpOnly; Secure; SameSite=None");
    }
}