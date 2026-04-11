package com.example.demo.util;

public final class AppUtil {

    private AppUtil() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String safe(String value, String fallback) {
        return isBlank(value) ? fallback : value.trim();
    }
}
