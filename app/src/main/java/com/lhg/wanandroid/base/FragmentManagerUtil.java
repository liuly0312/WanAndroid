package com.lhg.wanandroid.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class FragmentManagerUtil {

    private static FragmentManagerUtil fragmentManagerUtil = new FragmentManagerUtil();

    private static FragmentManager fragmentManager;

    public static FragmentManagerUtil getFragmentManager(AppCompatActivity appCompatActivity) {
        fragmentManager = appCompatActivity.getSupportFragmentManager();
        return fragmentManagerUtil;
    }

    public void addFragment(int resId, Fragment fragment) {
        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (Fragment f : fragments) {
                transaction.hide(f);
            }
            if (!fragment.isAdded()) {
                Log.e("LHG", "未添加过");
                transaction.add(resId, fragment);
            }
            transaction.show(fragment);
            transaction.commit();
        }
    }
}