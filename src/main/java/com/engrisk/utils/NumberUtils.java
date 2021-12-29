package com.engrisk.utils;

public class NumberUtils {
    public static boolean isLong(String s) {
        try {
            Long.parseLong(s.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long parseLong(String s) {
        return Long.parseLong(s.trim());
    }
}
