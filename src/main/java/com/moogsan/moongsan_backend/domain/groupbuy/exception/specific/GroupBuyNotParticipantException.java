package com.moogsan.moongsan_backend.domain.groupbuy.exception.specific;

import com.moogsan.moongsan_backend.domain.groupbuy.exception.base.GroupBuyException;
import com.moogsan.moongsan_backend.domain.groupbuy.exception.code.GroupBuyErrorCode;

public class GroupBuyNotParticipantException extends GroupBuyException {
    public GroupBuyNotParticipantException() {
        super(GroupBuyErrorCode.NOT_PARTICIPANT, "공구의 참여자만 요청 가능합니다.");
    }

    public GroupBuyNotParticipantException(String message) {
        super(GroupBuyErrorCode.NOT_PARTICIPANT, message);
    }
}