package com.moogsan.moongsan_backend.domain.user.service;

import com.moogsan.moongsan_backend.domain.user.dto.request.LoginRequest;
import com.moogsan.moongsan_backend.domain.user.dto.response.LoginResponse;
import com.moogsan.moongsan_backend.domain.user.entity.Token;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import com.moogsan.moongsan_backend.domain.user.exception.base.UserException;
import com.moogsan.moongsan_backend.domain.user.exception.code.UserErrorCode;
import com.moogsan.moongsan_backend.domain.user.repository.TokenRepository;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import com.moogsan.moongsan_backend.global.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND, "해당 이메일을 가진 사용자가 존재하지 않습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        // 탈퇴 복구 및 마지막 로그인 시간 기록
        if (user.getDeletedAt() != null) {
            user.setDeletedAt(null);
        }
        user.setLastLoginAt(); // 마지막 로그인 시간 갱신

        // 3. JWT 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        Long accessTokenExpireAt = jwtUtil.getAccessTokenExpireAt();
        Long refreshTokenExpireMillis = jwtUtil.getRefreshTokenExpireMillis();

        // 4. 기존 리프레시 토큰 제거
        refreshTokenRepository.deleteByUserId(user.getId());

        // 5. 새로운 리프레시 토큰 저장
        Token newToken = new Token(
                null,
                user.getId(),
                refreshToken,
                LocalDateTime.now().plusSeconds(refreshTokenExpireMillis / 1000)
        );
        refreshTokenRepository.save(newToken);

        // 쿠키 설정
        Cookie accessTokenCookie = new Cookie("AccessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) (accessTokenExpireAt / 1000));
        response.addCookie(accessTokenCookie);
        response.addHeader("Set-Cookie", "AccessToken=" + accessToken + "; HttpOnly; Secure; Path=/; SameSite=None");

        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (refreshTokenExpireMillis / 1000));
        response.addCookie(refreshTokenCookie);
        response.addHeader("Set-Cookie", "RefreshToken=" + refreshToken + "; HttpOnly; Secure; Path=/; SameSite=None");

        // 7. 최소 응답 정보 반환
        return new LoginResponse(
                user.getId(),
                user.getNickname(),
                user.getImageUrl()
        );
    }
}