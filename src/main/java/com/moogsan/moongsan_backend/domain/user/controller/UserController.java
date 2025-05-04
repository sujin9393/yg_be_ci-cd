package com.moogsan.moongsan_backend.domain.user.controller;

import com.moogsan.moongsan_backend.domain.user.dto.request.LoginRequest;
import com.moogsan.moongsan_backend.domain.user.dto.request.SignUpRequest;
import com.moogsan.moongsan_backend.domain.user.dto.response.LoginResponse;
import com.moogsan.moongsan_backend.domain.user.dto.response.CheckNicknameResponse;
import com.moogsan.moongsan_backend.domain.user.dto.response.UserProfileResponse;
import com.moogsan.moongsan_backend.domain.user.service.CheckNicknameService;
import com.moogsan.moongsan_backend.domain.user.service.LoginService;
import com.moogsan.moongsan_backend.domain.user.service.LogoutService;
import com.moogsan.moongsan_backend.domain.user.service.SignUpService;
import com.moogsan.moongsan_backend.domain.user.service.UserProfileService;
import com.moogsan.moongsan_backend.domain.user.service.WithdrawService;
import com.moogsan.moongsan_backend.domain.user.service.TokenRefreshService;
import com.moogsan.moongsan_backend.domain.user.entity.CustomUserDetails;
import com.moogsan.moongsan_backend.domain.WrapperResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final LoginService loginService;
    private final SignUpService signUpService;
    private final UserProfileService userProfileService;
    private final CheckNicknameService nicknameService;
    private final LogoutService logoutService;
    private final WithdrawService withdrawService;
    private final TokenRefreshService tokenRefreshService;

    @PostMapping("/users")
    public ResponseEntity<WrapperResponse<LoginResponse>> signUp(@Valid @RequestBody SignUpRequest request,
                                                                 HttpServletResponse response) {
        LoginResponse loginResponse = signUpService.signUp(request, response);
        return ResponseEntity.ok(
            WrapperResponse.<LoginResponse>builder()
                .message("회원가입이 완료되었습니다.")
                .data(loginResponse)
                .build()
        );
    }

    @PostMapping("/users/token")
    public ResponseEntity<WrapperResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest,
                                                                jakarta.servlet.http.HttpServletResponse response) {
        LoginResponse loginResponse = loginService.login(loginRequest, response);
        return ResponseEntity.ok(
            WrapperResponse.<LoginResponse>builder()
                .message("로그인이 완료되었습니다.")
                .data(loginResponse)
                .build()
        );
    }

    @GetMapping("/users/check-nickname")
    public ResponseEntity<WrapperResponse<CheckNicknameResponse>> checkNickname(@RequestParam String nickname) {
        CheckNicknameResponse response = nicknameService.checkNickname(nickname);

        if ("YES".equals(response.getIsDuplication())) {
            return ResponseEntity.status(409).body(
                    WrapperResponse.<CheckNicknameResponse>builder()
                            .message("이미 등록된 닉네임입니다.")
                            .data(response)
                            .build()
            );
        }

        return ResponseEntity.ok(
                WrapperResponse.<CheckNicknameResponse>builder()
                        .message("사용 가능한 닉네임입니다.")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/users/profile")
    public ResponseEntity<WrapperResponse<UserProfileResponse>> getProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserProfileResponse profile = userProfileService.getUserProfile(userDetails.getUser().getId());
        return ResponseEntity.ok(
            WrapperResponse.<UserProfileResponse>builder()
                .message("유저 정보 조회에 성공했습니다")
                .data(profile)
                .build());
    }

    @DeleteMapping("/users/token")
    public ResponseEntity<WrapperResponse<Void>> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        logoutService.logout(userDetails.getUser().getId());
        return ResponseEntity.ok(
            WrapperResponse.<Void>builder()
                    .message("로그아웃이 성공적으로 처리되었습니다.")
                    .data(null)
                    .build()
        );
    }

    @DeleteMapping("/users")
    public ResponseEntity<WrapperResponse<Void>> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
        withdrawService.withdraw(userDetails.getUser().getId());
        return ResponseEntity.ok(
                WrapperResponse.<Void>builder()
                        .message("회원탈퇴가 완료되었습니다.")
                        .data(null)
                        .build()
        );
    }

    @PostMapping("/users/token/refresh")
    public ResponseEntity<WrapperResponse<Void>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        tokenRefreshService.refreshAccessToken(request, response);
        return ResponseEntity.ok(
                WrapperResponse.<Void>builder()
                        .message("Access Token이 재발급되었습니다.")
                        .data(null)
                        .build()
        );
    }
}