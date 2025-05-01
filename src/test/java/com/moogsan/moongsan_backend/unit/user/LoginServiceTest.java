package com.moogsan.moongsan_backend.unit.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.moogsan.moongsan_backend.domain.user.dto.request.LoginRequest;
import com.moogsan.moongsan_backend.domain.user.dto.response.LoginResponse;
import com.moogsan.moongsan_backend.domain.user.entity.User;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import com.moogsan.moongsan_backend.domain.user.service.LoginService;
import com.moogsan.moongsan_backend.global.security.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("로그인 성공 시 JWT 발급 확인")
    void loginSuccess() {
        // 테스트용 로그인 요청 객체 생성
        LoginRequest request = new LoginRequest("testuser@example.com", "password123!");

        // 사용자 정보를 담은 더미 User 객체 생성
        User user = User.builder()
                .id(1L)
                .email("testuser@example.com")
                .password("encoded-password") // 실제 서비스에서는 인코딩된 비밀번호 저장됨
                .nickname("테스트유저")
                .build();

        // mock 객체들의 동작 정의
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user)); // 이메일로 사용자 조회
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true); // 비밀번호 검증
        when(jwtUtil.generateAccessToken(user)).thenReturn("mock-access-token"); // accessToken 생성
        when(jwtUtil.generateRefreshToken(user)).thenReturn("mock-refresh-token"); // refreshToken 생성
        when(jwtUtil.getAccessTokenExpireAt()).thenReturn(System.currentTimeMillis() + 3600000); // 토큰 만료시간 설정

        // 로그인 서비스 실행
        LoginResponse response = loginService.login(request);

        // 응답 데이터 검증
        assertEquals("로그인에 성공하였습니다.", response.getMessage()); // 메시지 검증
        assertEquals("테스트유저", response.getData().getNickname()); // 닉네임 검증
        assertEquals("mock-access-token", response.getData().getAccessToken()); // 액세스토큰 검증

        // 액세스토큰과 전체 응답을 콘솔에 출력
        System.out.println("JWT 발급 결과: " + response.getData().getAccessToken());
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonOutput = mapper.writeValueAsString(response);
            System.out.println("응답 JSON: " + jsonOutput); // 전체 응답 구조 출력
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}