package com.moogsan.moongsan_backend.global.security.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long accessTokenExpireMs;
    private long refreshTokenExpireMs;

    // setter를 명시적으로 만들어줘야 ConfigurationProperties가 값을 주입할 수 있음
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setAccessTokenExpireMs(long accessTokenExpireMs) {
        this.accessTokenExpireMs = accessTokenExpireMs;
    }

    public void setRefreshTokenExpireMs(long refreshTokenExpireMs) {
        this.refreshTokenExpireMs = refreshTokenExpireMs;
    }
}