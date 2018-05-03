package com.lhg.wanandroid.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.app.WanAndroidApp;
import com.lhg.wanandroid.base.BaseActivity;
import com.lhg.wanandroid.bean.UserResultBean;
import com.lhg.wanandroid.http.RetrofitFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseActivity {

    private TextView tv_to_register;
    private TextView tv_back;
    private TextView ed_username;
    private TextView ed_password;
    private TextView ed_re_password;
    private TextView tv_login;
    private TextView tv_page_title;
    private int mode; //0 代表登录。1 代表注册

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_to_register = findViewById(R.id.tv_to_register);
        tv_to_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_back = findViewById(R.id.tv_back);
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        ed_re_password = findViewById(R.id.ed_re_password);
        tv_login = findViewById(R.id.tv_login);
        tv_page_title = findViewById(R.id.tv_page_title);
    }

    @Override
    protected void loadData() {
        super.loadData();
        // 根据传进来的值判断当前是否为登录或者注册，默认登录
        mode = getIntent().getIntExtra("tag", 0);
        if (mode == 1) {
            tv_to_register.setVisibility(View.INVISIBLE);
            ed_re_password.setVisibility(View.VISIBLE);
            tv_login.setText("注册");
            tv_page_title.setText("注册 玩Android");
            tv_back.setText("返回");
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WanAndroidApp.getLoginStatus() == 0) {
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    intent.putExtra("tag", 1);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "已经登录，请勿重复注册", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ed_username.getText().toString();
                String password = ed_password.getText().toString();
                final String re_password = ed_re_password.getText().toString();
                if (mode == 0 && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    RetrofitFactory.getInstance().getLoginResult(username, password, new Observer<UserResultBean>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(final UserResultBean userResultBean) {
                            if (userResultBean.getErrorCode() == 0) {
                                WanAndroidApp.setLoginStatus(1);
                                WanAndroidApp.setUsername(userResultBean.getData().getUsername());
                            } else {
                                ed_password.setText("");
                                ed_username.setFocusable(true);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, userResultBean.getErrorCode() == 0 ? "登录成功" : userResultBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                    // 需要发送信息到MineFragment来确定登录信息
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                } else if (mode == 1 && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(re_password)) {
                    //注册
                    RetrofitFactory.getInstance().getRegisterResult(username, password, re_password, new Observer<UserResultBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(final UserResultBean userResultBean) {
                            if (userResultBean.getErrorCode() != 0) {
                                ed_password.setText("");
                                ed_re_password.setText("");
                                ed_username.setFocusable(true);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this).setTitle("提示").setMessage("注册成功,是否前往登录？")
                                        .setNegativeButton("取消", null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        });
                                builder.show();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, userResultBean.getErrorCode() == 0 ? "注册成功,请前往登录" : userResultBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (mode == 1 && TextUtils.isEmpty(re_password)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "请确认您的密码", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
