package com.moogsan.moongsan_backend.global.dto;

import java.time.ZonedDateTime;
import java.util.Map;

public record ErrorResponse(
        String code,
        String message,
        ZonedDateTime timestamp,
        Map<String, Object> details,
        String traceId
) {}
