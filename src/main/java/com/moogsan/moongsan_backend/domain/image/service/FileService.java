package com.moogsan.moongsan_backend.domain.image.service;

import com.moogsan.moongsan_backend.domain.image.dto.PresignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

/**
 * FileService: 이미지 업로드를 위한 Presigned URL 발급만 담당
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 업로드할 Presigned PUT URL 발급
     * @param fileName 클라이언트가 전달한 원본 파일명
     * @return key, url 정보가 담긴 PresignResponse
     */
    public PresignResponse presign(String fileName) {
        // 1) S3 key 생성 (images/{UUID}-{fileName})
        String key = "images/" + UUID.randomUUID() + "-" + fileName;

        // 2) Presign 요청 객체 생성
        PutObjectRequest objReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PutObjectPresignRequest preReq = PutObjectPresignRequest.builder()
                .putObjectRequest(objReq)
                .signatureDuration(Duration.ofMinutes(2))
                .build();

        // 3) URL 발급
        String url = s3Presigner.presignPutObject(preReq)
                .url().toString();

        // 4) DTO 반환 (imageId 없이 key + url)
        return new PresignResponse(key, url);
    }
}
