package com.moogsan.moongsan_backend.domain.groupbuy.repository;

import com.moogsan.moongsan_backend.domain.groupbuy.entity.GroupBuy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {
    //Optional<GroupBuy> findById(Long id);

    // 게시글 조회 - 공구 게시글 수정 전 정보, 공구 게시글 상세
    @EntityGraph(attributePaths = "images")
    Optional<GroupBuy> findWithImagesById(Long id);

    // 마감 임박 순 조회(without category)
    @EntityGraph(attributePaths = "images")
    Optional<List<GroupBuy>> findWithImagesByIsSoonTrueOrderByCreatedAtAsc(Pageable page);

    // 최신순 조회
    @EntityGraph(attributePaths = "images")
    Optional<List<GroupBuy>> findWithImages(Pageable page);

    // 리스트 조회(with category)
    List<GroupBuy> findByCategoryIdAndIdLessThan(Long categoryId, Long cursorId, Pageable page);

    // 리스트 조회(without category)
    List<GroupBuy> findByIdLessThan(Long cursorId, Pageable page);
}
