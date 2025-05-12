-- ============================================
-- Filename: V1__init_schema.sql
-- Purpose : 초기 테이블 정의 및 외래키 포함 스키마 생성
-- Encoding: UTF-8 (이모지, 한글 포함 대응)
-- ============================================

-- [1] 안전한 실행을 위한 기본 설정
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 카테고리 테이블
CREATE TABLE category (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 공동구매 게시글 테이블
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
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 공구글-카테고리 매핑 테이블
CREATE TABLE group_buy_category (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT UNSIGNED NOT NULL,
    group_buy_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    FOREIGN KEY (group_buy_id) REFERENCES group_buy(id) ON DELETE CASCADE,
    UNIQUE KEY uq_groupbuy_category (category_id, group_buy_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 이미지 테이블
CREATE TABLE image (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    image_key VARCHAR(765) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    image_resized_key VARCHAR(765) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    image_seq_no INT NOT NULL DEFAULT 0,
    thumbnail BOOLEAN NOT NULL DEFAULT FALSE,
    group_buy_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (group_buy_id) REFERENCES group_buy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- [3] 외래키 체크 복구
SET FOREIGN_KEY_CHECKS = 1;
