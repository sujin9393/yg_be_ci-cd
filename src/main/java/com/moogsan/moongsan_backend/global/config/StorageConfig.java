package com.moogsan.moongsan_backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StorageConfig {

    @Bean
    public Path uploadDir(@Value("${file.upload-dir}") String uploadDir) {
        return Paths.get(uploadDir);
    }

    @Bean
    public String publicBaseUrl(@Value("${file.public-base-url}") String publicBaseUrl) {
        return publicBaseUrl;
    }
}

