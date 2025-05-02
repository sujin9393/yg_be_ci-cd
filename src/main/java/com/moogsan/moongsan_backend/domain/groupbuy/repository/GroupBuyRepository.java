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

    // 게시글 조회 - 공구 게시글 수정 전 정보, 공구 게시글 상세
    @EntityGraph(attributePaths = "images")
    Optional<GroupBuy> findWithImagesById(Long id);

    // ----------------------------------------
    // 1) 최신순(createdAt DESC) 커서 페이징
    // (1-1) 카테고리 무관
    @Query("""
       SELECT g
         FROM GroupBuy g
        WHERE g.id < :cursorId
        ORDER BY g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByCreatedCursor(
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    // (1-2) 특정 카테고리만
    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE gbc.category.id = :categoryId
          AND g.id < :cursorId
        ORDER BY g.createdAt DESC, g.id DESC
    """)
    List<GroupBuy> findByCategoryAndCreatedCursor(
            @Param("categoryId") Long categoryId,
            @Param("cursorId")   Long cursorId,
            Pageable pageable
    );

    // ----------------------------------------
    // 2) 마감 임박순(dueSoon = true, 최신순)
    // (2-1) 카테고리 무관
    @Query("""
       SELECT g
         FROM GroupBuy g
        WHERE g.dueSoon = true
          AND (
                g.createdAt < :lastCreatedAt
             OR (g.createdAt = :lastCreatedAt AND g.id < :lastId)
          )
        ORDER BY g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByDueSoonCursor(
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            @Param("lastId")        Long lastId,
            Pageable pageable
    );

    // (2-2) 특정 카테고리만
    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE gbc.category.id = :categoryId
          AND g.dueSoon = true
          AND (
                g.createdAt < :lastCreatedAt
             OR (g.createdAt = :lastCreatedAt AND g.id < :lastId)
          )
        ORDER BY g.createdAt DESC, g.id DESC
       """)
    List<GroupBuy> findByCategoryAndDueSoonCursor(
            @Param("categoryId")    Long categoryId,
            @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
            @Param("lastId")        Long lastId,
            Pageable pageable
    );

    // ----------------------------------------
    // 3) 가격 낮은 순(unitPrice ASC, 커서 비교 포함)
    // (3-1) 카테고리 무관
    @Query("""
       SELECT g
         FROM GroupBuy g
        WHERE (
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

    // (3-2) 특정 카테고리만
    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE gbc.category.id = :categoryId
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
    List<GroupBuy> findByCategoryAndPriceAscCursor(
            @Param("categoryId")      Long categoryId,
            @Param("lastPrice")       Integer lastPrice,
            @Param("lastCreatedAt")   LocalDateTime lastCreatedAt,
            @Param("lastId")          Long lastId,
            Pageable pageable
    );
}
