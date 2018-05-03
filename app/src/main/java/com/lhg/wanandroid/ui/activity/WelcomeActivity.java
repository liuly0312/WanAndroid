package com.lhg.wanandroid.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    private TextView tv_app_name;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_app_name = findViewById(R.id.tv_app_name);
    }

    @Override
    protected void loadData() {
        super.loadData();
        tv_app_name.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }
}
