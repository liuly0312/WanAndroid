package com.lhg.wanandroid.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.utils.TranslucentBarUtil;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        TranslucentBarUtil.immersiveMode(this, getResources().getColor(R.color.colorPrimary), false);
        setContentView(getContentViewResId());
        ActivityManager.getInstance().addActivity(this);
        initView();
        loadData();
        initListener();
    }

    /**
     * 获取contentView 资源id
     */
    protected abstract int getContentViewResId();

    /**
     * 初始化View
     */
    protected void initView() {

    }

    protected void loadData() {

    }

    /**
     * 初始化View的监听器
     */
    protected void initListener() {

    }

    /**
     * 短时间展示一个Toast
     *
     * @param message
     */
    protected void showToastShort(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 长时间展示一个Toast
     *
     * @param message
     */
    protected void showToastLong(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}
