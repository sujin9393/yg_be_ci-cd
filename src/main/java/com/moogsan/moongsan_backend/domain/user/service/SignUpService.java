package com.moogsan.moongsan_backend.domain.user.service;

import com.moogsan.moongsan_backend.domain.user.dto.request.SignUpRequest;
import com.moogsan.moongsan_backend.domain.user.dto.response.LoginResponse;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import com.moogsan.moongsan_backend.domain.user.entity.Token;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import com.moogsan.moongsan_backend.domain.user.repository.TokenRepository;
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
public class SignUpService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화기
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponse signUp(SignUpRequest request, HttpServletResponse response) {
        validateDuplicateUser(request);

        // 회원정보 DB에 저장
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
        userRepository.flush();
        String accessToken = jwtUtil.generateAccessToken(savedUser);
        String refreshToken = jwtUtil.generateRefreshToken(savedUser);
        Long accessTokenExpireAt = jwtUtil.getAccessTokenExpireAt();

        // AccessToken 쿠키로 설정
        Cookie accessTokenCookie = new Cookie("AccessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) (accessTokenExpireAt / 1000));
        response.addCookie(accessTokenCookie);
        response.addHeader("Set-Cookie", "AccessToken=" + accessToken + "; HttpOnly; Secure; Path=/; SameSite=None");

        // RefreshToken 쿠키로 설정
        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (jwtUtil.getRefreshTokenExpireMillis() / 1000));
        response.addCookie(refreshTokenCookie);
        response.addHeader("Set-Cookie", "RefreshToken=" + refreshToken + "; HttpOnly; Secure; Path=/; SameSite=None");

        // Token DB에 저장
        Token newToken = new Token(
            null,
            savedUser.getId(),
            refreshToken,
            LocalDateTime.now().plusSeconds(jwtUtil.getRefreshTokenExpireMillis() / 1000)
        );
        tokenRepository.save(newToken);

        return new LoginResponse(
                savedUser.getId(),
                savedUser.getNickname(),
                savedUser.getImageUrl()
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