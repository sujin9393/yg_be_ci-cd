package com.moogsan.moongsan_backend.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email; // 이메일

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password; // 비밀번호

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}