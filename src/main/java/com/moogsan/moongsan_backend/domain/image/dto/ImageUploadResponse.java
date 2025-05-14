package com.moogsan.moongsan_backend.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageUploadResponse {
    private String message;
    private List<String> imageUrls;
}
