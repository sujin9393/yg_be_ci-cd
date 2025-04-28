## PR 제목 예시 : feat(groupbuy): 커서 기반 페이지네이션 도입
> **⚠️ 예시 내용은 확인 후 삭제해주세요.**

## 🔎 작업 개요
offset 기반 페이지네이션의 성능·중복 조회 문제를 해결하기 위해 cursor 방식 도입

## 🛠️ 주요 변경 사항
- `GroupBuyService#getByCursor(cursor, size)` 메서드 추가  
- Controller에서 `?cursor` 파라미터 처리 및 `nextCursor` 반환  
- `GroupBuyCursorResponse` DTO 추가  
- OpenAPI 스펙에 cursor 예시 반영  
- 단위 테스트 8건, 통합 테스트 3건 추가  

## ✅ 검증 방법
1. 단위·통합 테스트 모두 통과  
2. `GET /api/group-buys` 호출 시 응답에 `nextCursor` 확인  
3. `GET /api/group-buys?cursor={nextCursor}` 로 다음 페이지 정상 조회

## 🔍 머지 전 확인사항  
- [ ] 테스트 통과 여부

## ➕ 이슈 링크
- [#17 커서 기반 페이지네이션 도입](https://github.com/100-hours-a-week/14-YG-BE/issues/17)
