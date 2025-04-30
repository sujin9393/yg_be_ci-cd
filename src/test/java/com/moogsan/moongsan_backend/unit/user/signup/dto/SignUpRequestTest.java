package com.moogsan.moongsan_backend.unit.user.dto;

import com.moogsan.moongsan_backend.domain.user.signup.dto.SignUpRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestTest {

    private Validator validator;

    /**
     * 각 테스트 실행 전 Validator 인스턴스를 초기화한다.
     * ValidatorFactory를 통해 표준 Validator 구현체를 생성함.
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("회원가입 요청 필드 검증")
    class ValidationTests {

        @Test
        @DisplayName("이메일이 비어있으면 실패한다")
        void emailBlank() {
            // 이메일을 빈 문자열로 설정한 요청 생성
            SignUpRequest request = new SignUpRequest(
                    "", "Password1!", "nickname", "name", "01012345678", "은행명", "1234567890"
            );

            // 유효성 검증 수행
            Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

            // 결과에 email 필드의 에러가 포함되어 있는지 확인
            assertThat(violations).extracting("propertyPath").extracting(Object::toString)
                    .contains("email");
        }

        @Test
        @DisplayName("비밀번호가 규칙에 맞지 않으면 실패한다")
        void passwordPattern() {
            // 특수문자 없이 단순한 비밀번호 설정
            SignUpRequest request = new SignUpRequest(
                    "test@example.com", "password", "nickname", "name", "01012345678", "은행명", "1234567890"
            );

            Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

            assertThat(violations).extracting("propertyPath").extracting(Object::toString)
                    .contains("password");
        }

        @Test
        @DisplayName("닉네임이 2자 미만이면 실패한다")
        void nicknameSize() {
            // 1글자 닉네임 사용
            SignUpRequest request = new SignUpRequest(
                    "test@example.com", "Password1!", "a", "name", "01012345678", "은행명", "1234567890"
            );

            Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

            assertThat(violations).extracting("propertyPath").extracting(Object::toString)
                    .contains("nickname");
        }

        @Test
        @DisplayName("전화번호가 11자 초과이면 실패한다")
        void phoneNumberPattern() {
            // 전화번호를 12자리로 설정 (허용된 범위는 10~11자리)
            SignUpRequest request = new SignUpRequest(
                    "test@example.com", "Password1!", "nickname", "name", "010123456789", "은행명", "1234567890"
            );

            Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

            assertThat(violations).extracting("propertyPath").extracting(Object::toString)
                    .contains("phoneNumber");
        }

        @Test
        @DisplayName("모든 값이 정상일 때 성공한다")
        void validSignUpRequest() {
            // 유효한 값들로 요청을 구성
            SignUpRequest request = new SignUpRequest(
                    "test@example.com", "Password1!", "nickname", "name", "01012345678", "은행명", "1234567890"
            );

            // 검증 결과가 비어 있어야 성공
            Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }
}