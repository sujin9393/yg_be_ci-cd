package com.moogsan.moongsan_backend.domain.user.repository;

import com.moogsan.moongsan_backend.domain.user.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

    boolean existsByUserIdAndGroupBuyId(Long userId, Long groupBuyId);

    Optional<Wish> findByUserIdAndGroupBuyId(Long userId, Long groupBuyId);

}
