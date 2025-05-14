package com.moogsan.moongsan_backend.global.util;

import io.micrometer.common.util.StringUtils;

public final class ValidatorUtil {

    private ValidatorUtil() {
        // 인스턴스화 방지
    }

    /**
     * 문자열이 null이 아니고, trim했을 때 길이가 0보다 큰지 검사
     * @param str 검사할 문자열
     * @return null이 아니고, 공백이 아닌 문자열이면 true
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotBlank(str);
    }

    /**
     * 문자열이 null이거나, trim했을 때 길이가 0인 경우 true
     * @param str 검사할 문자열
     * @return null이거나, 공백 문자열이면 true
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isBlank(str);
    }
}
