package com.moogsan.moongsan_backend.unit.user.controller;

import com.moogsan.moongsan_backend.domain.user.signup.service.SignUpService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UserController 단위 테스트에서 필요한 Mock Bean을 명시적으로 등록하는 설정 클래스입니다.
 * WebMvcTest 환경에서 실제 Bean 대신 Mockito Mock 객체를 사용하여
 * 테스트 컨텍스트를 완성합니다.
 */
@Configuration
public class UserControllerTestConfig {

    /**
     * UserService에 대한 Mockito Mock 객체를 Spring Bean으로 등록합니다.
     * UserControllerTest 실행 시 실제 구현체 대신 이 Mock이 주입되어 사용됩니다.
     *
     * @return UserService의 Mockito Mock 인스턴스
     */
    @Bean
    public SignUpService userService() {
        return Mockito.mock(SignUpService.class);
    }
}