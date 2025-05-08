package com.moogsan.moongsan_backend.domain.image.controller;

import com.moogsan.moongsan_backend.domain.image.dto.PresignResponse;
import com.moogsan.moongsan_backend.domain.image.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 이미지 엔티티를 먼저 생성한 뒤, S3 PUT용 Presigned URL을 발급합니다.
     * - 이미지 레코드 생성 시점에 imageId와 S3 key를 DB에 저장합니다.
     * - 반환된 URL로 클라이언트가 직접 S3에 업로드할 수 있습니다.
     *
     * @param fileName 원본 파일명
     */
    @GetMapping("/presign")
    public ResponseEntity<PresignResponse> presign(
            @RequestParam String fileName
    ) {
        // 1) 이미지 레코드(DB) 생성 + key 생성
        //    FileService.createImageRecord(...)은 imageId와 key를 반환합니다.
        PresignResponse presign = fileService.presign(fileName);

        // 2) imageId, key, url을 포함해 응답
        return ResponseEntity.ok(presign);
    }
}
