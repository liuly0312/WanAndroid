package com.lhg.wanandroid.ui.module.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lhg.wanandroid.R;
import com.lhg.wanandroid.app.WanAndroidApp;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.ui.activity.LoginActivity;
import com.lhg.wanandroid.ui.activity.NextActivity;
import com.lhg.wanandroid.ui.activity.SettingsActivity;
import com.lhg.wanandroid.utils.ContantUtils;

import java.util.Random;

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View v_login;
    private TextView tv_collect_article;
    private TextView tv_collect_website;
    private TextView tv_focus_words;
    private TextView tv_setting;
    private TextView tv_not_login;
    private TextView tv_user_name;
    private TextView tv_about;
    private ImageView user_head;
    private String username;

    public MineFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (WanAndroidApp.getLoginStatus() == 1) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                username = arguments.getString("username");
                if (tv_user_name != null && !TextUtils.isEmpty(username)) {
                    tv_user_name.setText(username);
                }
                if (user_head != null) {
                    Glide.with(mActivity).load(ContantUtils.authorHead[new Random().nextInt(ContantUtils.authorHead.length)]).into(user_head);
                }
            }
        }
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        super.initView();
        v_login = findViewById(R.id.v_login);
        tv_collect_article = findViewById(R.id.tv_collect_article);
        tv_collect_website = findViewById(R.id.tv_collect_website);
        tv_focus_words = findViewById(R.id.tv_focus_words);
        tv_about = findViewById(R.id.tv_about);
        tv_setting = findViewById(R.id.tv_setting);
        user_head = findViewById(R.id.user_head);
        tv_not_login = findViewById(R.id.tv_not_login);
        tv_user_name = findViewById(R.id.tv_user_name);
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        v_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WanAndroidApp.getLoginStatus() == 0) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra("tag", 0);
                    startActivity(intent);
                } else {
                    Toast.makeText(mActivity, "已经登录，请勿点击", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, SettingsActivity.class));
            }
        });
        tv_not_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WanAndroidApp.setUsername("");
                WanAndroidApp.setLoginStatus(0);
                Toast.makeText(mActivity, "已经退出登录", Toast.LENGTH_SHORT).show();
                tv_user_name.setText("点击登录账号");
                user_head.setImageResource(R.mipmap.ic_user_head);
            }
        });

        tv_collect_article.setOnClickListener(this);
        tv_collect_website.setOnClickListener(this);
        tv_focus_words.setOnClickListener(this);
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, NextActivity.class);
                intent.putExtra("tag", "About");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (WanAndroidApp.getLoginStatus() == 1) {
            Intent intent = new Intent(mActivity, NextActivity.class);
            switch (v.getId()) {
                case R.id.tv_collect_article:
                    intent.putExtra("tag", "CollectArticle");
                    break;
                case R.id.tv_collect_website:
                    intent.putExtra("tag", "CollectWebsite");
                    break;
                case R.id.tv_focus_words:
                    intent.putExtra("tag", "CollectWords");
                    break;
            }
            startActivity(intent);
        } else {
            startActivity(new Intent(mActivity, LoginActivity.class));
        }
    }
}
