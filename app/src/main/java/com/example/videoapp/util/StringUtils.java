package com.example.videoapp.util;

public class StringUtils {
    public static boolean isEmpty(String account) {
        if (account == null || account.length() <= 0){
            return true;
        }else {
            return false;
        }
    }
}
