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
  ( 1, '코카콜라 이쏘용',  '콜라콜라 업소용, 1.25L, 12개',
    '스테디 담아가세요~~',
    'https://www.coupang.com/vp/products/6357109541?itemId=18712158063&vendorItemId=89577901976&q=코카콜라+업소용&itemsCount=36&searchId=db6969b11417302&rank=2&searchRank=2&isAddedCart=',
       10900, 909, 12, 1, 11, 1,
    FALSE, NULL, '2025-05-11 9:00:00','카카오테크 교육장','2025-05-21 17:00:00',
    0,0,0,'OPEN', NULL, 1,'2025-05-11 9:00:00','2025-05-11 9:00:00'),
  (2, '✨ 부르르 제로사이다 올킬 ✨','부르르 제로사이다, 250ml, 30개',
    '이보다 착한 가격 있을 수 없음',
    'https://www.coupang.com/vp/products/2358334844?itemId=20262964509&vendorItemId=72077082095&q=부르르+제로+30&itemsCount=36&searchId=fe6fd7ae2113875&rank=6&searchRank=6&isAddedCart=',
    11700, 390, 30, 10, 20, 10,
    TRUE, NULL, '2025-05-11 10:00:00','카카오테크 교육장','2025-05-22 17:00:00',
    0,0,0,'OPEN', NULL, 1,'2025-05-11 10:00:00','2025-05-11 10:00:00'),
  ( 3, '🍜 짜라짜라짜짜짜 짜파게티 정품 🍜',   '짜파게티 140g, 40개',
     '40개 대용량 팩으로 면치기 끝판왕 도전🕺',
      'https://www.coupang.com/vp/products/6215299058?itemId=12409660215&vendorItemId=3054115373&q=짜파게티+40개&itemsCount=36&searchId=cda9a0721253928&rank=0&searchRank=0&isAddedCart=',
      31550, 3155,  40, 10, 0, 10,
      TRUE, NULL, '2025-05-11 11:00:00','카카오테크 교육장','2025-05-23 17:00:00',
      0,0,0,'CLOSED', NULL, 1,'2025-05-11 11:00:00','2025-05-11 11:00:00'),
  ( 4, '김치전 꼬다리맛 입덕하세요!','동원 양반 김치맛 김부각 50g, 16개',
    'SNS 랭킹 1위 김부각 공동구매로 더 저렴하게 만나보세요. 친구 소환도 잊지 마세요! 📣',
    'https://www.coupang.com/vp/products/7170900116?itemId=23945666335&vendorItemId=91037017195&q=김치맛+김부각&itemsCount=36&searchId=162202ca347249&rank=1&searchRank=1&isAddedCart=',
    31900, 1994, 16, 4, 0, 10,
    TRUE, NULL, '2025-05-11 12:00:00','카카오테크 교육장','2025-05-24 17:00:00',
    0,0,0,'ENDED', NULL, 1,'2025-05-11 12:00:00','2025-05-11 12:00:00');

-- 4) image (대표 썸네일만 1~40)
INSERT INTO image (
    id, image_key, image_resized_key, image_seq_no, thumbnail, group_buy_id
) VALUES
  ( 1, 'images/5b8b1535-9793-4cdb-bc1d-e239c1bae8f0', NULL, 0, TRUE,  1),
  ( 2, 'images/56fac65f-10e3-461a-a2fd-af7f2e8ad02b', NULL, 0, TRUE,  2),
  ( 3, 'images/e9975c2a-4afb-47e8-a6b6-41a074c2b3aa', NULL, 1, FALSE,  2),
  ( 4, 'images/5bbca2e8-cc39-4d82-8d51-ac2c42b8735d', NULL, 0, TRUE,  3),
  ( 5, 'images/42119b97-e59f-491b-8572-c16d673076f6', NULL, 0, TRUE,  4);

-- 5) group_buy_category (1만 moongsanPick)
INSERT INTO group_buy_category (
    id, group_buy_id, category_id
) VALUES
  ( 1,  1, 1);

-- 6) orders
INSERT INTO orders (
    id, user_id, post_id, status, price, quantity, name, deleted_count, created_at, modified_at, deleted_at
) VALUES (
    1, 1, 40, 'PAID', 2000, 8, '박지은', 0, '2025-05-04 13:00:00', '2025-05-04 13:00:00', NULL
);

-- 7) 외래키 제약 ON
SET FOREIGN_KEY_CHECKS = 1;
