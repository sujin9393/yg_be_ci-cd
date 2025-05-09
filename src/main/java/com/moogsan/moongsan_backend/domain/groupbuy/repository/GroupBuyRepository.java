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

    // 공구 마감 조건 기반 조회
    List<GroupBuy> findByPostStatusAndDueDateBefore(String postStatus, LocalDateTime now);

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
    // 0) Non-Cursor 메서드 (첫 페이지용)
    // ----------------------------------------------------

    // 0-1) 최신순(createdAt DESC, id DESC)
    @Query("""
       SELECT g
         FROM GroupBuy g
        ORDER BY g.createdAt DESC, g.id DESC
       """
    )
    List<GroupBuy> findAllByCreatedOrder(Pageable pageable);

    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE gbc.category.id = :categoryId
        ORDER BY g.createdAt DESC, g.id DESC
       """
    )
    List<GroupBuy> findByCategoryCreatedOrder(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    // 0-2) 마감 임박순(dueSoon = true, createdAt DESC, id DESC)
    @Query("""
       SELECT g
         FROM GroupBuy g
        WHERE g.dueSoon = true
        ORDER BY g.createdAt DESC, g.id DESC
       """
    )
    List<GroupBuy> findAllByDueSoonOrder(Pageable pageable);

    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE gbc.category.id = :categoryId
          AND g.dueSoon = true
        ORDER BY g.createdAt DESC, g.id DESC
       """
    )
    List<GroupBuy> findByCategoryDueSoonOrder(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    // 0-3) 가격 낮은 순(unitPrice ASC, createdAt DESC, id DESC)
    @Query("""
       SELECT g
         FROM GroupBuy g
        ORDER BY g.unitPrice ASC, g.createdAt DESC, g.id DESC
       """
    )
    List<GroupBuy> findAllByPriceOrder(Pageable pageable);

    @Query("""
       SELECT g
         FROM GroupBuy g
         JOIN g.groupBuyCategories gbc
        WHERE gbc.category.id = :categoryId
        ORDER BY g.unitPrice ASC, g.createdAt DESC, g.id DESC
       """
    )
    List<GroupBuy> findByCategoryPriceOrder(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );


    // ----------------------------------------------------
    // 1) 최신순(createdAt DESC, id DESC) 커서 페이징
    // ----------------------------------------------------

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


    // ----------------------------------------------------
    // 2) 마감 임박순(dueSoon = true, 최신순) 커서 페이징
    // ----------------------------------------------------

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


    // ----------------------------------------------------
    // 3) 가격 낮은 순(unitPrice ASC) 커서 페이징
    // ----------------------------------------------------

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
