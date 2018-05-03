package com.lhg.wanandroid.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class CommonUtils {

    @SuppressLint("SimpleDateFormat")
    public static String getFormatDate(long date) {
        return new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").format(date);
    }

    public static String handleContainH(String str) {
        boolean isBegin = false;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '<') {
                isBegin = true;
            } else if (c == '>') {
                isBegin = false;
            } else {
                if (!isBegin) {
                    stringBuilder.append(str.charAt(i));
                }
            }
        }
        return stringBuilder.toString();
    }
}
