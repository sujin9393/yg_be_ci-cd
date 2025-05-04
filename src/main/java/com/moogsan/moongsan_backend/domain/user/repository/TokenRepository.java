package com.moogsan.moongsan_backend.domain.user.repository;

import com.moogsan.moongsan_backend.domain.user.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    void deleteByUserId(Long userId);

    Optional<Token> findByUserId(Long userId);
}