package com.moogsan.moongsan_backend.domain.groupbuy.controller;

import com.moogsan.moongsan_backend.domain.groupbuy.dto.command.response.ImageUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/image")
@Slf4j
public class ImageController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @PostMapping
    public ResponseEntity<ImageUploadResponse> uploadImages(
            @RequestParam("imageFiles") List<MultipartFile> imageFiles) {

        if (imageFiles == null || imageFiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ImageUploadResponse("upload_fail_client", Collections.emptyList()));
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                if (file.isEmpty()) continue;

                String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String storedFilename = UUID.randomUUID() + extension;

                Path filePath = uploadPath.resolve(storedFilename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 실제 클라이언트 접근 가능한 URL 경로
                imageUrls.add("/uploads/" + storedFilename);
            }

            if (imageUrls.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ImageUploadResponse("upload_fail_client", Collections.emptyList()));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ImageUploadResponse("upload_success", imageUrls));

        } catch (IOException e) {
            log.error("Image upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ImageUploadResponse("upload_fail_server", Collections.emptyList()));
        }
    }
}
