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

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long parseLong(String s) {
        return Long.parseLong(s.trim());
    }

    public static Float parseFloat(String s) {
        return Float.parseFloat(s.trim());
    }
}
