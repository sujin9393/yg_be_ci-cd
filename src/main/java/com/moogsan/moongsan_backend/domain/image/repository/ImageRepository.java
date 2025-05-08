package com.moogsan.moongsan_backend.domain.image.repository;

import com.moogsan.moongsan_backend.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
