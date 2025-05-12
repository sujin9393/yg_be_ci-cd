-- 0) 외래키 제약 OFF
SET FOREIGN_KEY_CHECKS = 0;

-- 1) users
INSERT INTO users (
    id, email, password, nickname, name,
    phone_number, account_bank, account_number, image_key,
    type, status, joined_at, modified_at, logout_at, deleted_at
) VALUES (
      1,
      'admin@moongsan.com',      -- 관리자용 이메일
      'admin1234!',              -- 관리자용 비밀번호 (개발용 평문)
      'adminmaster',             -- 닉네임
      '관리자 계정',                -- 실명
      '01099998888',             -- 전화번호
      '카카오뱅크',                 -- 관리자 계좌 은행
      '110123456789',            -- 계좌 번호
      NULL,                      -- 프로필 이미지 없음
      'ADMIN',                   -- 사용자 유형
      'ACTIVE',                  -- 상태
      '2025-05-01 09:00:00',     -- 가입 시각
      '2025-05-01 09:00:00',     -- 수정 시각
      NULL,                      -- 로그아웃 시각 없음
      NULL                       -- 삭제 시각 없음
);

-- 2) category
INSERT INTO category (id, name) VALUES
  (1, 'moongsanPick');

-- 3) group_buy
INSERT INTO group_buy (
    id, title, name, description, url,
    price, unit_price, total_amount, host_quantity, left_amount, unit_amount,
    due_soon, badge, due_date, location, pickup_date,
    wish_count, view_count, participant_count, post_status, pickup_change_reason,
    user_id, created_at, modified_at
) VALUES
  (1, '코카콜라 이쏘용', '콜라콜라 업소용, 1.25L, 12개',
   '무더운 여름, 시원한 한 잔이 필요할 때! 업소에서도 집에서도 모두 만족하는 1.25L 대용량 코카콜라를 공동구매로 더욱 저렴하게 만나보세요. 부담 없이 넉넉하게 즐기세요!',
   'https://www.coupang.com/vp/products/6357109541?itemId=18712158063&vendorItemId=89577901976&q=코카콜라+업소용&itemsCount=36&searchId=db6969b11417302&rank=2&searchRank=2&isAddedCart=',
   10900, 909, 12, 1, 11, 1,
   FALSE, NULL, '2025-05-21 9:00:00','카카오테크 교육장','2025-05-21 17:00:00',
   0,0,0,'OPEN', NULL, 1,'2025-05-11 9:00:00','2025-05-11 9:00:00'),

  (2, '✨ 부르르 제로사이다 올킬 ✨','부르르 제로사이다, 250ml, 30개',
   '당류 0g, 칼로리 부담 없이 즐기는 탄산음료의 끝판왕! 다이어터, 건강 챙기는 분들 모두를 위한 선택! 250ml 소용량이라 휴대도 간편하고, 30개 대용량이라 나눠 마시기에도 최고!',
   'https://www.coupang.com/vp/products/2358334844?itemId=20262964509&vendorItemId=72077082095&q=부르르+제로+30&itemsCount=36&searchId=fe6fd7ae2113875&rank=6&searchRank=6&isAddedCart=',
   11700, 390, 30, 10, 20, 10,
   TRUE, NULL, '2025-05-13 10:00:00','카카오테크 교육장','2025-05-22 17:00:00',
   0,0,0,'OPEN', NULL, 1,'2025-05-11 10:00:00','2025-05-11 10:00:00'),

  (3, '🍜 짜라짜라짜짜짜 짜파게티 정품 🍜', '짜파게티 140g, 40개',
   '짜장라면의 대표주자, 짜파게티를 대용량으로 쟁여보세요! 진한 풍미와 쫄깃한 면발을 40봉지로 든든하게! 자취생, 가족 모두 만족할 가성비 최강 면치기 아이템!',
   'https://www.coupang.com/vp/products/6215299058?itemId=12409660215&vendorItemId=3054115373&q=짜파게티+40개&itemsCount=36&searchId=cda9a0721253928&rank=0&searchRank=0&isAddedCart=',
   31550, 3155, 40, 10, 0, 10,
   TRUE, NULL, '2025-05-11 11:00:00','카카오테크 교육장','2025-05-12 12:21:00',
   0,0,0,'CLOSED', NULL, 1,'2025-05-10 11:00:00','2025-05-10 11:00:00'),

  (4, '김치전 꼬다리맛 입덕하세요!','동원 양반 김치맛 김부각 50g, 16개',
   '바삭한 식감과 깊은 김치 풍미의 조화! 한번 먹으면 멈출 수 없는 중독적인 맛! SNS에서 인기 폭발한 김부각을 공동구매로 더 저렴하게 즐기세요. 맥주 안주로도 찰떡!',
   'https://www.coupang.com/vp/products/7170900116?itemId=23945666335&vendorItemId=91037017195&q=김치맛+김부각&itemsCount=36&searchId=162202ca347249&rank=1&searchRank=1&isAddedCart=',
   31900, 1994, 16, 4, 0, 10,
   TRUE, NULL, '2025-05-01 12:00:00','카카오테크 교육장','2025-05-12 17:00:00',
   0,0,0,'ENDED', NULL, 1,'2025-05-01 12:00:00','2025-05-01 12:00:00');

-- 4) image (대표 썸네일만 1~40)
INSERT INTO image (
    id, image_key, image_resized_key, image_seq_no, thumbnail, group_buy_id
) VALUES
  ( 1, 'images/5b8b1535-9793-4cdb-bc1d-e239c1bae8f0', NULL, 0, TRUE,  1),
  ( 2, 'images/56fac65f-10e3-461a-a2fd-af7f2e8ad02b', NULL, 0, TRUE,  2),
  ( 3, 'images/8a7c38e0-70d1-44dd-b217-02fafe35c7e1', NULL, 1, FALSE, 2),
  ( 4, 'images/cbdd55d5-507b-4d16-93ba-f4cb924fd4ae', NULL, 0, TRUE,  3),
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
