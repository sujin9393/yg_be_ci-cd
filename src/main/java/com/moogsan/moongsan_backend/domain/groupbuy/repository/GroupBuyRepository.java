package com.moogsan.moongsan_backend.domain.groupbuy.repository;

import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {
    //Optional<GroupBuy> findById(Long id);

    // 공구 마감 조건 기반 조회 (백그라운드 API용)
    List<GroupBuy> findByPostStatusAndDueDateBefore(String postStatus, LocalDateTime now);

    // 공구 종료 조건 기반 조회 (백그라운드 API용)
    List<GroupBuy> findByPostStatusAndPickupDateBefore(String postStatus, LocalDateTime now);

    // 공구 주최 리스트 첫 조회
    List<GroupBuy> findByUser_IdAndPostStatus(Long userId, String postStatus, Pageable pageable);

    // 공구 주최 리스트 이어서 조회
    List<GroupBuy> findByUser_IdAndPostStatusAndIdLessThan(
            Long userId,
            String postStatus,
            Long cursorId,
            Pageable pageable);

    // 게시글 조회 - 공구 게시글 수정 전 정보, 공구 게시글 상세
    @EntityGraph(attributePaths = "images")
    Optional<GroupBuy> findWithImagesById(Long id);

    // ----------------------------------------------------
    // 0) Non-Cursor 메서드 (첫 페이지용, ENDED 제외)
    // ----------------------------------------------------

    /** 0-1) 최신순(createdAt DESC, id DESC), ENDED 제외 */
    @Query("""
       SELECT g
         FROM GroupBuy g
        WHERE g.postStatus <> 'ENDED'
        ORDER BY g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findAllCreatedOrder(Pageable pageable);

    /** 0-1-a) + 카테고리 필터 */
    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE g.postStatus <> 'ENDED'
          AND gbc.category.id = :categoryId
        ORDER BY g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByCategoryCreatedOrder(@Param("categoryId") Long categoryId, Pageable pageable);

    /** 0-2) 마감 임박순 정렬 (판매율 높은 순), ENDED 제외 */
    @Query(value = """
    SELECT *,
           (total_amount - left_amount) * 100 / total_amount AS sell_ratio
      FROM group_buy
     WHERE post_status = 'OPEN'
     ORDER BY sell_ratio DESC,
              created_at DESC,
              id DESC
    """, nativeQuery = true)
    List<GroupBuy> findEndingSoon(Pageable pageable);




    /** 0-2-a) + 카테고리 필터 */
    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE g.postStatus = 'OPEN'
          AND gbc.category.id = :categoryId
        ORDER BY g.dueSoon DESC, g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByCategoryDueSoonOrder(@Param("categoryId") Long categoryId, Pageable pageable);

    /** 0-3) 가격 낮은 순(unitPrice ASC), ENDED 제외 */
    @Query("""
       SELECT g
         FROM GroupBuy g
        WHERE g.postStatus <> 'ENDED'
        ORDER BY g.unitPrice ASC, g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findAllPriceOrder(Pageable pageable);

    /** 0-3-a) + 카테고리 필터 */
    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE g.postStatus <> 'ENDED'
          AND gbc.category.id = :categoryId
        ORDER BY g.unitPrice ASC, g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByCategoryPriceOrder(@Param("categoryId") Long categoryId, Pageable pageable);

    // ----------------------------------------------------
    // 1) 최신순(createdAt DESC, id DESC) 커서 페이징 (ENDED 제외)
    // ----------------------------------------------------

    @Query("""
       SELECT g
         FROM GroupBuy g
        WHERE g.postStatus <> 'ENDED'
          AND g.id < :cursorId
        ORDER BY g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByCreatedCursor(@Param("cursorId") Long cursorId, Pageable pageable);

    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE g.postStatus <> 'ENDED'
          AND gbc.category.id = :categoryId
          AND g.id < :cursorId
        ORDER BY g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByCategoryCreatedCursor(
            @Param("categoryId") Long categoryId,
            @Param("cursorId")   Long cursorId,
            Pageable pageable
    );

    // ----------------------------------------------------
    // 2) 마감 임박순 (dueSoon DESC, then createdAt/​id DESC) 커서 페이징 (ENDED 제외)
    // ----------------------------------------------------

    @Query("""
   SELECT g
     FROM GroupBuy g
    WHERE g.postStatus = 'OPEN'
      AND (
            g.createdAt < :lastCreatedAt
         OR (g.createdAt = :lastCreatedAt AND g.id < :lastId)
          )
    ORDER BY g.dueSoon DESC, g.createdAt DESC, g.id DESC
   """)
    List<GroupBuy> findByEndingSoonCursor(
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            @Param("lastId")        Long lastId,
            Pageable pageable
    );

    @Query("""
   SELECT g
     FROM GroupBuy g
     JOIN g.groupBuyCategories gbc
    WHERE g.postStatus = 'OPEN'
      AND gbc.category.id = :categoryId
      AND (
            g.createdAt < :lastCreatedAt
         OR (g.createdAt = :lastCreatedAt AND g.id < :lastId)
          )
    ORDER BY g.dueSoon DESC, g.createdAt DESC, g.id DESC
   """)
    List<GroupBuy> findByCategoryEndingSoonCursor(
            @Param("categoryId")    Long categoryId,
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            @Param("lastId")        Long lastId,
            Pageable pageable
    );

    // ----------------------------------------------------
    // 3) 가격 오름차순(unitPrice ASC) 커서 페이징 (ENDED 제외)
    // ----------------------------------------------------

    @Query("""
       SELECT g
         FROM GroupBuy g
        WHERE g.postStatus <> 'ENDED'
          AND (
                g.unitPrice > :lastPrice
             OR (g.unitPrice = :lastPrice
                 AND (
                   g.createdAt < :lastCreatedAt
                OR (g.createdAt = :lastCreatedAt AND g.id < :lastId)
                 )
               )
          )
        ORDER BY g.unitPrice ASC, g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByPriceAscCursor(
            @Param("lastPrice")       Integer lastPrice,
            @Param("lastCreatedAt")   LocalDateTime lastCreatedAt,
            @Param("lastId")          Long lastId,
            Pageable pageable
    );

    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE g.postStatus <> 'ENDED'
          AND gbc.category.id = :categoryId
          AND (
                g.unitPrice > :lastPrice
             OR (g.unitPrice = :lastPrice
                 AND (
                   g.createdAt < :lastCreatedAt
                OR (g.createdAt = :lastCreatedAt AND g.id < :lastId)
                 )
               )
          )
        ORDER BY g.unitPrice ASC, g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByCategoryPriceAscCursor(
            @Param("categoryId")      Long categoryId,
            @Param("lastPrice")       Integer lastPrice,
            @Param("lastCreatedAt")   LocalDateTime lastCreatedAt,
            @Param("lastId")          Long lastId,
            Pageable pageable
    );
}
