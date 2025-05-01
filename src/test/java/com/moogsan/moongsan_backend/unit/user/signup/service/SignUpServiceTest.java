//package com.moogsan.moongsan_backend.unit.user.signup.service;
//
//import com.moogsan.moongsan_backend.domain.user.dto.request.SignUpRequest;
//import com.moogsan.moongsan_backend.domain.user.dto.response.LoginResponse;
//import com.moogsan.moongsan_backend.domain.user.entity.User;
//import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
//import com.moogsan.moongsan_backend.domain.user.service.SignUpService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
///**
// * UserService 클래스의 회원가입 로직을 검증하는 단위 테스트 클래스
// * - 정상적인 회원가입 성공 플로우
// * - 이메일, 닉네임, 전화번호 중복으로 인한 실패 플로우
// */
//class SignUpServiceTest {
//
//    @Mock
//    private UserRepository userRepository; // User 저장소를 Mock 처리
//
//    @Mock
//    private PasswordEncoder passwordEncoder; // 비밀번호 암호화기를 Mock 처리
//
//    @InjectMocks
//    private SignUpService signUpService; // 위의 Mock 객체들을 주입받아 테스트할 대상
//
//    /**
//     * 각 테스트 실행 전에 Mockito mock 초기화
//     */
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Nested
//    @DisplayName("회원가입 테스트")
//    class SignUpTests {
//
//        @Test
//        @DisplayName("회원가입이 정상적으로 완료된다")
//        void signUpSuccess() {
//            // given - 정상적인 회원가입 요청 생성
//            SignUpRequest request = SignUpRequest.builder()
//                    .email("test@example.com")
//                    .password("Password1!")
//                    .nickname("nickname")
//                    .name("name")
//                    .phoneNumber("01012345678")
//                    .accountBank("은행명")
//                    .accountNumber("1234567890")
//                    .build();
//
//            // 이메일, 닉네임, 전화번호 중복 없음
//            when(userRepository.existsByEmail(anyString())).thenReturn(false);
//            when(userRepository.existsByNickname(anyString())).thenReturn(false);
//            when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
//
//            // 비밀번호 암호화 Mock 설정
//            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//
//            // 저장되는 User 객체를 그대로 반환
//            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//            // when - 회원가입 메서드 호출
//            LoginResponse savedUser = signUpService.signUp(request);
//
//            // then - 저장된 User 객체 검증
//            assertThat(savedUser.getEmail()).isEqualTo(request.getEmail());
//            assertThat(savedUser.getNickname()).isEqualTo(request.getNickname());
//            assertThat(savedUser.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
//        }
//
//        @Test
//        @DisplayName("이메일이 중복되면 회원가입이 실패한다")
//        void signUpDuplicateEmailFail() {
//            // given - 이메일 중복 상황 가정
//            SignUpRequest request = SignUpRequest.builder()
//                    .email("test@example.com")
//                    .password("Password1!")
//                    .nickname("nickname")
//                    .name("name")
//                    .phoneNumber("01012345678")
//                    .accountBank("은행명")
//                    .accountNumber("1234567890")
//                    .build();
//
//            // 이메일 중복 발생
//            when(userRepository.existsByEmail(anyString())).thenReturn(true);
//
//            // when & then - IllegalArgumentException 발생 및 메시지 검증
//            assertThatThrownBy(() -> signUpService.signUp(request))
//                    .isInstanceOf(IllegalArgumentException.class)
//                    .hasMessage("이미 등록된 이메일입니다.");
//        }
//
//        @Test
//        @DisplayName("닉네임이 중복되면 회원가입이 실패한다")
//        void signUpDuplicateNicknameFail() {
//            // given - 닉네임 중복 상황 가정
//            SignUpRequest request = SignUpRequest.builder()
//                    .email("test@example.com")
//                    .password("Password1!")
//                    .nickname("nickname")
//                    .name("name")
//                    .phoneNumber("01012345678")
//                    .accountBank("은행명")
//                    .accountNumber("1234567890")
//                    .build();
//
//            // 이메일은 통과, 닉네임 중복 발생
//            when(userRepository.existsByEmail(anyString())).thenReturn(false);
//            when(userRepository.existsByNickname(anyString())).thenReturn(true);
//
//            // when & then - IllegalArgumentException 발생 및 메시지 검증
//            assertThatThrownBy(() -> signUpService.signUp(request))
//                    .isInstanceOf(IllegalArgumentException.class)
//                    .hasMessage("이미 등록된 닉네임입니다.");
//        }
//
//        @Test
//        @DisplayName("전화번호가 중복되면 회원가입이 실패한다")
//        void signUpDuplicatePhoneNumberFail() {
//            // given - 전화번호 중복 상황 가정
//            SignUpRequest request = SignUpRequest.builder()
//                    .email("test@example.com")
//                    .password("Password1!")
//                    .nickname("nickname")
//                    .name("name")
//                    .phoneNumber("01012345678")
//                    .accountBank("은행명")
//                    .accountNumber("1234567890")
//                    .build();
//
//            // 이메일과 닉네임은 통과, 전화번호 중복 발생
//            when(userRepository.existsByEmail(anyString())).thenReturn(false);
//            when(userRepository.existsByNickname(anyString())).thenReturn(false);
//            when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);
//
//            // when & then - IllegalArgumentException 발생 및 메시지 검증
//            assertThatThrownBy(() -> signUpService.signUp(request))
//                    .isInstanceOf(IllegalArgumentException.class)
//                    .hasMessage("이미 등록된 전화번호입니다.");
//        }
//    }
//}