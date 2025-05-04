package com.moogsan.moongsan_backend.domain.groupbuy.scheduler;

import com.moogsan.moongsan_backend.domain.groupbuy.service.GroupBuyCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GroupBuyScheduler {

    private final GroupBuyCommandService groupBuyService;

    // 공구 게시글 dueDate 기반 자동 공구 마감 스케줄러(매일 정각에 작동)
    @Scheduled(cron = "0 0 0 * * *")
    public void closeExpiredGroupBuys() {
        LocalDateTime now = LocalDateTime.now();
        groupBuyService.closePastDueGroupBuys(now);
    }
}
