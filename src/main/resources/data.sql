-- 0) 외래키 제약 OFF
SET FOREIGN_KEY_CHECKS = 0;

-- 1) users
INSERT INTO users (
    id, email, password, nickname, name,
    phone_number, account_bank, account_number, image_key,
    type, status, joined_at, modified_at, logout_at, deleted_at
) VALUES (
    1,
    'test@example.com',
    'test1234',
    'testnick',
    '테스트 유저',
    '01012345678',
    '국민은행',
    '12345678901234',
    NULL,
    'USER',
    'ACTIVE',
    '2025-05-01 09:00:00',
    '2025-05-01 09:00:00',
    NULL,
    NULL
);

-- 2) category
INSERT INTO category (id, name) VALUES
  (1, 'moongsanPick');

-- 3) group_buy (40개)
INSERT INTO group_buy (
    id, title, name, description, url,
    price, unit_price, total_amount, host_quantity, left_amount, unit_amount,
    due_soon, badge, due_date, location, pickup_date,
    wish_count, view_count, participant_count, post_status, pickup_change_reason,
    user_id, created_at, modified_at
) VALUES
  ( 1, '🎉 신상 공구 런칭 #1',  '더미 상품 #1',
    '🎁 인기 상품을 함께 구매하고 추가 할인 혜택을 받아보세요. 놓치지 마세요! 👍',
    'https://example.com/1',   10000, 1000, 10, 1, 9, 1,
    FALSE, NULL, '2025-06-01 12:00:00','테스트 장소','2025-06-05 12:00:00',
    0,0,0,'OPEN', NULL, 1,'2025-03-27 00:00:00','2025-06-05 09:00:00'),
  (2, '🚴‍♂️ 레저 공구 #33','더미 상품 #33',
    '🏕️ 야외 활동용품, 공동구매로 준비 끝! 이번 주말 떠나볼까요? 🚴‍♂️',
    'https://example.com/33', 42000, 42000, 370, 340, 30, 10,
    TRUE, NULL, '2025-07-03 12:00:00','테스트 장소','2025-07-07 12:00:00',
    0,0,0,'OPEN', NULL, 1,'2025-03-11 00:00:00','2025-05-04 09:00:00'),
  ( 3, '💎 프리미엄 공구 #3',   '더미 상품 #3',
     '💝 고급 상품을 합리적인 가격에! 함께 모아 더 큰 만족을 경험해 보세요. 😉',
      'https://example.com/3',   12000, 12000,  70, 70, 0, 3,
      TRUE, NULL, '2025-06-03 12:00:00','테스트 장소','2025-06-07 12:00:00',
      0,0,0,'CLOSED', NULL, 1,'2025-03-03 00:00:00','2025-06-03 09:00:00'),
  ( 4, '🛒 베스트셀러 공구 #4','더미 상품 #4',
    '📈 판매 1위 상품! 공동구매로 더 저렴하게 만나보세요. 친구 소환도 잊지 마세요! 📣',
    'https://example.com/4',   13000, 13000, 80, 80, 0, 10,
    TRUE, NULL, '2025-05-10 12:00:00','테스트 장소','2025-05-11 12:00:00',
    0,0,0,'ENDED', NULL, 1,'2025-04-26 00:00:00','2025-04-26 09:00:00');

-- 4) image (대표 썸네일만 1~40)
INSERT INTO image (
    id, image_key, image_resized_key, image_seq_no, thumbnail, group_buy_id
) VALUES
  ( 1, '/uploads/4748bfa5-4fe7-462b-a4e0-0380efd6713a.png', NULL, 0, TRUE,  1),
  ( 2, '/uploads/4748bfa5-4fe7-462b-a4e0-0380efd6713a.png', NULL, 0, TRUE,  2),
  ( 3, '/uploads/4748bfa5-4fe7-462b-a4e0-0380efd6713a.png', NULL, 0, TRUE,  3),
  ( 4, '/uploads/4748bfa5-4fe7-462b-a4e0-0380efd6713a.png', NULL, 0, TRUE,  4);

-- 5) group_buy_category (1만 moongsanPick)
INSERT INTO group_buy_category (
    id, group_buy_id, category_id
) VALUES
  ( 1,  1, 1);

-- 6) orders
INSERT INTO orders (
    id, user_id, post_id, status, price, quantity, name, deleted_count, created_at, deleted_at
) VALUES (
    1, 1, 1, 'PAID', 3000, 3, '박지은', 0, '2025-05-04 13:00:00', NULL
);

-- 7) 외래키 제약 ON
SET FOREIGN_KEY_CHECKS = 1;
