package com.lhg.wanandroid.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class WanAndroidApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static int login_status = 0; //登录状态，0为未登录，1为登录
    private static String username;
    private static String password;

    @Override
    public void onCreate() {
        super.onCreate();
        int i = 0;
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static int getLoginStatus() {
        return login_status;
    }

    public static void setLoginStatus(int login_status) {
        WanAndroidApp.login_status = login_status;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        WanAndroidApp.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        WanAndroidApp.password = password;
    }
}
