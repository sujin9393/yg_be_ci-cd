package com.moogsan.moongsan_backend.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 회원가입 요청 시 클라이언트로부터 전달받는 데이터를 담는 DTO 클래스.
 * 각 필드에 대해 유효성 검증이 적용되어 있다.
 */
@Getter // 모든 필드에 대해 Getter 메서드를 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일의 형식이 올바르지 않습니다.")
    private String email; // 사용자 이메일 주소

    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[!@#$%^*+=-])[A-Za-z\\d!@#$%^*+=-]{8,30}$",
            message = "비밀번호는 8~30자이며, 반드시 하나 이상의 특수문자(!@#$%^*+=-)를 포함해야 합니다."
    )
    private String password; // 로그인용 비밀번호

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 12, message = "닉네임은 2자 이상 12자 이하여야 합니다.")
    private String nickname; // 사용자 닉네임

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다.")
    private String name; // 사용자 실명

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 하이픈(-) 없이 10~11자리 숫자여야 합니다.")
    private String phoneNumber; // 사용자 전화번호

    @NotBlank(message = "은행명은 필수입니다.")
    private String accountBank; // 계좌 은행명

    @NotBlank(message = "계좌번호는 필수입니다.")
    @Pattern(regexp = "^\\d+$", message = "계좌번호는 숫자만 입력해야 합니다.")
    private String accountNumber; // 계좌 번호

    private String imageUrl; // 프로필 이미지 URL (회원가입 시에는 null)

    /**
     * 회원가입 시 필수 입력값을 받는 생성자.
     * imageUrl은 회원가입 시 null로 초기화된다.
     */
    public SignUpRequest(String email, String password, String nickname, String name, String phoneNumber, String accountBank, String accountNumber) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.accountBank = accountBank;
        this.accountNumber = accountNumber;
        this.imageUrl = null;
    }
}