-- ============================================
-- Filename: V1__init_schema.sql
-- Purpose : 초기 테이블 정의 및 외래키 포함 스키마 생성
-- Encoding: UTF-8 (이모지, 한글 포함 대응)
-- ============================================

-- [1] 안전한 실행을 위한 기본 설정
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 유저 테이블
CREATE TABLE users (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '유저 아이디',
    email VARCHAR(255) NOT NULL COMMENT '이메일 (최대 254자)',
    nickname VARCHAR(60) DEFAULT NULL COMMENT '닉네임 (한글 최대 20자)',
    name VARCHAR(36) DEFAULT NULL COMMENT '실명',
    phone_number VARCHAR(11) DEFAULT NULL COMMENT '전화번호 (하이픈 제외)',
    password VARCHAR(60) DEFAULT NULL COMMENT '비밀번호 (영어, 숫자, 특수문자 포함, 8~30자)',
    account_bank VARCHAR(30) DEFAULT NULL COMMENT '은행명',
    account_number VARCHAR(32) DEFAULT NULL COMMENT '계좌 번호',
    image_key VARCHAR(512) DEFAULT NULL COMMENT '프로필 이미지',
    type VARCHAR(30) NOT NULL DEFAULT 'USER' COMMENT '사용자 유형 (USER, ADMIN)',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE' COMMENT '사용자 상태 (ACTIVE, SUSPENDED, DEACTIVATED)',
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입 시각',
    modified_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '정보 수정 시각',
    logout_at TIMESTAMP NULL DEFAULT NULL COMMENT '1년 이상 휴면 시 저장되는 로그아웃 시각',
    deleted_at TIMESTAMP NULL DEFAULT NULL COMMENT '탈퇴 시각 (Soft Delete 후 Hard Delete 예정)',
    INDEX idx_email (email),
    INDEX idx_status (status),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2. 토큰 테이블
CREATE TABLE token (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '토큰 아이디',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '유저 아이디',
    token VARCHAR(2048) NOT NULL COMMENT 'refreshToken 문자열',
    expires DATETIME NOT NULL COMMENT '만료 일시',
    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_token (token(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 3. 카테고리 테이블
CREATE TABLE category (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 공동구매 게시글 테이블
CREATE TABLE group_buy (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    name VARCHAR(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    url TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    price INT UNSIGNED NOT NULL,
    unit_price INT UNSIGNED NOT NULL,
    total_amount INT UNSIGNED NOT NULL,
    left_amount INT UNSIGNED NOT NULL,
    unit_amount INT UNSIGNED NOT NULL,
    host_quantity INT UNSIGNED NOT NULL,
    description VARCHAR(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    due_soon BOOLEAN NOT NULL DEFAULT FALSE,
    badge VARCHAR(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    due_date DATETIME NOT NULL,
    location VARCHAR(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    pickup_date DATETIME NOT NULL,
    wish_count INT UNSIGNED NOT NULL DEFAULT 0,
    view_count INT UNSIGNED NOT NULL DEFAULT 0,
    participant_count INT UNSIGNED NOT NULL DEFAULT 0,
    post_status VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'OPEN',
    pickup_change_reason VARCHAR(765) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    user_id BIGINT UNSIGNED NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 공구글-카테고리 매핑 테이블
CREATE TABLE group_buy_category (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT UNSIGNED NOT NULL,
    group_buy_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    FOREIGN KEY (group_buy_id) REFERENCES group_buy(id) ON DELETE CASCADE,
    UNIQUE KEY uq_groupbuy_category (category_id, group_buy_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. 이미지 테이블
CREATE TABLE image (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    image_key VARCHAR(765) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    image_resized_key VARCHAR(765) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    image_seq_no INT NOT NULL DEFAULT 0,
    thumbnail BOOLEAN NOT NULL DEFAULT FALSE,
    group_buy_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (group_buy_id) REFERENCES group_buy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. 위시(관심) 테이블
CREATE TABLE wish (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '관심 아이디',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '유저 아이디',
    post_id BIGINT UNSIGNED NOT NULL COMMENT '공구 아이디',
    CONSTRAINT fk_wish_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_wish_post
        FOREIGN KEY (post_id) REFERENCES group_buy(id)
        ON DELETE CASCADE,
    UNIQUE KEY uq_user_post (user_id, post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- [3] 외래키 체크 복구
SET FOREIGN_KEY_CHECKS = 1;