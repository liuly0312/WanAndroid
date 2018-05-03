package com.lhg.wanandroid.base;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
    /**
     * activity的管理类 （单例模式）
     */
    private static ActivityManager activityManager;

    private List<AppCompatActivity> activityList;

    private ActivityManager() {
        activityList = new ArrayList<>();
    }

    /**
     * 用于获取ActivityManager的唯一对象
     *
     * @return ActivityManager
     */
    public static synchronized ActivityManager getInstance() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    /**
     * 向List<AppCompatActivity>中加入一个Activity
     *
     * @param appCompatActivity
     */
    public void addActivity(AppCompatActivity appCompatActivity) {
        if (activityList != null) {
            activityList.add(appCompatActivity);
        }
    }

    /**
     * 从List<AppCompatActivity>中移除掉一个Activity
     *
     * @param appCompatActivity
     */
    public void removeActivity(AppCompatActivity appCompatActivity) {
        if (activityList != null) {
            appCompatActivity.finish();
            activityList.remove(appCompatActivity);
        }
    }
}
