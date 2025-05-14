package com.moogsan.moongsan_backend.domain.user.service;

import com.moogsan.moongsan_backend.domain.user.dto.response.CheckDuplicationResponse;
import com.moogsan.moongsan_backend.domain.user.exception.base.UserException;
import com.moogsan.moongsan_backend.domain.user.exception.code.UserErrorCode;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckDuplicationService {

    private final UserRepository userRepository;

    public CheckDuplicationResponse checkNickname(String nickname) {
        // 닉네임 유효성 검사
        if (nickname == null || nickname.trim().length() < 2 || nickname.length() > 12) {
            throw new UserException(UserErrorCode.INVALID_INPUT, "닉네임이 올바르지 않습니다.\n2자 이상 12자 이하의 닉네임을 입력해주세요.");
        }

        // 닉네임 중복 여부 반환
        boolean exists = userRepository.existsByNickname(nickname);
        return new CheckDuplicationResponse(exists ? "YES" : "NO");
    }

    public CheckDuplicationResponse checkEmail(String email) {
        // 이메일 유효성 검사
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new UserException(UserErrorCode.INVALID_INPUT, "이메일의 형식이 올바르지 않습니다.");
        }

        // 이메일 중복 여부 반환
        boolean exists = userRepository.existsByEmail(email);
        return new CheckDuplicationResponse(exists ? "YES" : "NO");
    }
}