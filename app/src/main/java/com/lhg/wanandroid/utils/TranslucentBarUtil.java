package com.lhg.wanandroid.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class TranslucentBarUtil {

    public static void immersiveMode(Activity activity, int color, boolean isHyaline) {
        int SYSTEM_VERSION = Build.VERSION.SDK_INT;
        Window window = activity.getWindow();
        if (!isHyaline) {
            if (SYSTEM_VERSION >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(color);
            } else if (SYSTEM_VERSION >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
                View statusBarView = new View(window.getContext());
                int statusBarHeight = 0;
                Resources res = activity.getResources();
                int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = res.getDimensionPixelSize(resourceId);
                }
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
                params.gravity = Gravity.TOP;
                statusBarView.setLayoutParams(params);
                statusBarView.setBackgroundColor(color);
                decorViewGroup.addView(statusBarView);
            }
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}