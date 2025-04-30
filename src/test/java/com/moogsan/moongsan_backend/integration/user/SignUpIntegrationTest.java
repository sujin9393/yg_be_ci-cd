package com.moogsan.moongsan_backend.integration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moogsan.moongsan_backend.domain.user.dto.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.moogsan.moongsan_backend.global.security.jwt.JwtUtil;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SignUpIntegrationTest {

    @TestConfiguration
    static class TestJwtUtilConfiguration {
        @Bean
        public JwtUtil jwtUtil() {
            JwtUtil mock = Mockito.mock(JwtUtil.class);
            Mockito.when(mock.generateAccessToken(Mockito.any())).thenReturn("fakeAccessToken");
            Mockito.when(mock.generateRefreshToken(Mockito.any())).thenReturn("fakeRefreshToken");
            Mockito.when(mock.getAccessTokenExpireAt()).thenReturn(System.currentTimeMillis() + 3600000L);
            return mock;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("회원가입 API 통합 테스트")
    void signUpTest() throws Exception {
        // given
        SignUpRequest request = new SignUpRequest(
                "user" + System.currentTimeMillis() + "@example.com",
                "password123!",
                "닉네임",
                "홍길동",
                "01012345678",
                "신한은행",
                "110123456789",
                null // imageUrl
        );

        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isOk());

        // 결과 출력
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("회원가입 응답: " + responseBody);
    }
}