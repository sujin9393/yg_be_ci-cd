package com.moogsan.moongsan_backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * AWS SDK v2 기반 S3 설정 클래스
 * - S3Client: 동기(블로킹) S3 작업 수행용
 * - S3Presigner: Presigned URL 생성용
 */
@Configuration
public class AwsSdkV2S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * S3Client 빈 등록
     * - 동기 방식(블로킹) S3 연산 지원
     * - 버킷 객체 업로드/다운로드, 목록 조회 등
     */
    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    /**
     * S3Presigner 빈 등록
     * - Presigned URL(GET/PUT) 생성 지원
     * - 객체 접근 권한이 필요한 클라이언트에 안전한 URL 제공
     */
    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
}

