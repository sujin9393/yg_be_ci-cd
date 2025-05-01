package com.moogsan.moongsan_backend.domain.user.service;

import com.moogsan.moongsan_backend.domain.user.dto.response.CheckNicknameResponse;
import com.moogsan.moongsan_backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckNicknameService {

    private final UserRepository userRepository;

    public CheckNicknameResponse checkNickname(String nickname) {
        boolean exists = userRepository.existsByNickname(nickname);
        return new CheckNicknameResponse(exists ? "YES" : "NO");
    }
}