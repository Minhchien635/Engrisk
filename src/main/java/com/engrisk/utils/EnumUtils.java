package com.engrisk.utils;

import com.engrisk.enums.SexType;

import java.util.HashMap;

public class EnumUtils {
    public static HashMap<SexType, String> genderStringMap = new HashMap<>();

    static {
        genderStringMap.put(SexType.MALE, "Nam");
        genderStringMap.put(SexType.FEMALE, "Nữ");
        genderStringMap.put(SexType.OTHER, "Khác");
    }

    public static String toString(SexType gender) {
        return genderStringMap.get(gender);
    }
}
