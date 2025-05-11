package com.moogsan.moongsan_backend.domain.groupbuy.scheduler;

import com.moogsan.moongsan_backend.domain.groupbuy.service.GroupBuyCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GroupBuyScheduler {

    private final GroupBuyCommandService groupBuyCommandService;

    // 공구 게시글 dueDate 기반 자동 공구 마감 스케줄러(매일 정각에 작동)
    @Scheduled(cron = "0 0 0 * * *")
    public void closeExpiredGroupBuys() {
        LocalDateTime now = LocalDateTime.now();
        groupBuyCommandService.closePastDueGroupBuys(now);
    }

    // 공구 게시글 pickupDate 기반 자동 공구 종료 스케줄러 (매일 01:00에 작동)
    @Scheduled(cron = "0 0 1 * * *")
    public void endPastPickupGroupBuys() {
        LocalDateTime now = LocalDateTime.now();
        groupBuyCommandService.endPastPickupGroupBuys(now);
    }
}
