package com.shizy.ftd.util;

public class StringUtils {

    public static boolean isBlack(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().equals("");
    }

    public static boolean notBlack(String str) {
        return !isBlack(str);
    }
}
