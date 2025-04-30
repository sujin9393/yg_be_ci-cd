package com.moogsan.moongsan_backend.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 시 클라이언트로부터 전달받는 데이터를 담는 DTO 클래스.
 * 이메일과 비밀번호 입력값에 대해 유효성 검증을 적용한다.
 */
@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email; // 로그인할 사용자 이메일

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password; // 로그인할 사용자 비밀번호

    /**
     * 이메일과 비밀번호를 모두 입력받는 생성자.
     *
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}