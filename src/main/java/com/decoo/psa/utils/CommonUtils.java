package com.decoo.psa.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class CommonUtils {
    public static String getenv(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.length() == 0 ? defaultValue : value;
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return System.currentTimeMillis() + uuid + uuid.substring(0, 19);
    }

    public static String getRandomSecret() {
        return getRandomStr(64);
    }

    public static String getDefaultKeyByUserId(Long userId) {
        return userId + getRandomStr(32) + System.currentTimeMillis();
    }

    public static String getRandomStr(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
