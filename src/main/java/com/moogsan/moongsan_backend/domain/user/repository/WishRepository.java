package com.moogsan.moongsan_backend.domain.user.repository;

import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import com.moogsan.moongsan_backend.domain.user.entity.Wish;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

    Optional<Wish> findByUserIdAndGroupBuyId(Long userId, Long groupBuyId);

    boolean existsByUserIdAndGroupBuyId(Long userId, Long groupBuyId);

    // 공구 리스트 목록에 속한 공구가 관심 공구인지 확인
    @Query("select w.groupBuy.id from Wish w where w.user.id = :userId and w.groupBuy.id in :ids")
    List<Long> findWishedGroupBuyIds(@Param("userId") Long userId,
                                     @Param("ids") List<Long> groupBuyIds);

    // 관심 공구 리스트 첫 조회
    @Query("""
       select w.groupBuy
         from Wish w
        where w.user.id = :userId
          and w.groupBuy.postStatus = :postStatus
     order by w.groupBuy.id desc
    """)
    List<GroupBuy> findGroupBuysByUserAndStatus(
            @Param("userId") Long userId,
            @Param("postStatus") String postStatus,
            Pageable pageable
    );

    // 관심 공구 리스트 이어서 조회
    @Query("""
       select w.groupBuy
         from Wish w
        where w.user.id = :userId
          and w.groupBuy.postStatus = :postStatus
          and w.groupBuy.id < :cursorId
     order by w.groupBuy.id desc
    """)
    List<GroupBuy> findGroupBuysByUserAndStatusBeforeId(
            @Param("userId") Long userId,
            @Param("postStatus") String postStatus,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

}
