package com.moogsan.moongsan_backend.unit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moogsan.moongsan_backend.domain.user.signup.controller.SignUpController;
import com.moogsan.moongsan_backend.domain.user.signup.dto.SignUpRequest;
import com.moogsan.moongsan_backend.domain.user.signup.service.SignUpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SignUpController.class) // UserController만 로드하여 테스트
@AutoConfigureMockMvc(addFilters = false) // Spring Security 필터 제거 (403 방지)
@Import(UserControllerTestConfig.class) // 테스트용 UserService Bean을 직접 등록
class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc; // 실제 HTTP 요청을 흉내내는 테스트 도구

    @Autowired
    private SignUpService signUpService; // 테스트 Config에서 주입한 Mock 객체

    @Autowired
    private ObjectMapper objectMapper; // 객체를 JSON 문자열로 변환하기 위한 Jackson 유틸

    @Test
    @DisplayName("회원가입 요청이 정상적으로 처리된다")
    void signUpSuccess() throws Exception {
        // given - 정상 입력값 구성
        SignUpRequest request = new SignUpRequest(
                "test@example.com", "Password1!", "nickname", "name",
                "01012345678", "은행명", "1234567890"
        );

        // userService.signUp 호출 시 null 반환하도록 설정 (단순 성공 흐름)
        Mockito.when(signUpService.signUp(Mockito.any(SignUpRequest.class)))
                .thenReturn(null);

        // when & then - 정상 요청 시 201 Created 반환 기대
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("이메일 형식이 잘못되면 400 Bad Request가 발생한다")
    void signUpInvalidEmail() throws Exception {
        // given - 잘못된 이메일 형식
        SignUpRequest request = new SignUpRequest(
                "invalid-email", "Password1!", "nickname", "name",
                "01012345678", "은행명", "1234567890"
        );

        // when & then - 유효성 검사 실패로 400 기대
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("비밀번호 형식이 잘못되면 400 Bad Request가 발생한다")
    void signUpInvalidPassword() throws Exception {
        // given - 특수문자가 없는 비밀번호
        SignUpRequest request = new SignUpRequest(
                "test@example.com", "password", "nickname", "name",
                "01012345678", "은행명", "1234567890"
        );

        // when & then - 유효성 검사 실패로 400 기대
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("닉네임이 너무 짧으면 400 Bad Request가 발생한다")
    void signUpShortNickname() throws Exception {
        // given - 닉네임 1자 입력
        SignUpRequest request = new SignUpRequest(
                "test@example.com", "Password1!", "a", "name",
                "01012345678", "은행명", "1234567890"
        );

        // when & then - 유효성 검사 실패로 400 기대
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("전화번호 형식이 잘못되면 400 Bad Request가 발생한다")
    void signUpInvalidPhoneNumber() throws Exception {
        // given - 하이픈이 포함된 전화번호
        SignUpRequest request = new SignUpRequest(
                "test@example.com", "Password1!", "nickname", "name",
                "010-1234-5678", "은행명", "1234567890"
        );

        // when & then - 유효성 검사 실패로 400 기대
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일이 중복되면 409 Conflict가 발생한다")
    void signUpDuplicateEmail() throws Exception {
        // given - 중복 이메일 입력
        SignUpRequest request = new SignUpRequest(
                "duplicate@example.com", "Password1!", "nickname", "name",
                "01012345678", "은행명", "1234567890"
        );

        // 중복 이메일 시 예외 발생 시뮬레이션
        Mockito.when(signUpService.signUp(Mockito.any(SignUpRequest.class)))
                .thenThrow(new IllegalArgumentException("이미 등록된 이메일입니다."));

        // when & then - 409 Conflict 기대
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("닉네임이 중복되면 409 Conflict가 발생한다")
    void signUpDuplicateNickname() throws Exception {
        // given - 중복 닉네임 입력
        SignUpRequest request = new SignUpRequest(
                "test@example.com", "Password1!", "dupNick", "name",
                "01012345678", "은행명", "1234567890"
        );

        // 중복 닉네임 시 예외 발생 시뮬레이션
        Mockito.when(signUpService.signUp(Mockito.any(SignUpRequest.class)))
                .thenThrow(new IllegalArgumentException("이미 등록된 닉네임입니다."));

        // when & then - 409 Conflict 기대
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("전화번호가 중복되면 409 Conflict가 발생한다")
    void signUpDuplicatePhoneNumber() throws Exception {
        // given - 중복 전화번호 입력
        SignUpRequest request = new SignUpRequest(
                "test@example.com", "Password1!", "nickname", "name",
                "01099998888", "은행명", "1234567890"
        );

        // 중복 전화번호 시 예외 발생 시뮬레이션
        Mockito.when(signUpService.signUp(Mockito.any(SignUpRequest.class)))
                .thenThrow(new IllegalArgumentException("이미 등록된 전화번호입니다."));

        // when & then - 409 Conflict 기대
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}