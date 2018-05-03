package com.lhg.wanandroid.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.app.WanAndroidApp;
import com.lhg.wanandroid.base.BaseActivity;
import com.lhg.wanandroid.base.FragmentManagerUtil;
import com.lhg.wanandroid.http.RetrofitFactory;
import com.lhg.wanandroid.ui.module.index.IndexFragment;
import com.lhg.wanandroid.ui.module.mine.MineFragment;
import com.lhg.wanandroid.ui.module.project.ProjectFragment;
import com.lhg.wanandroid.ui.module.system.SystemCastFragment;
import com.lhg.wanandroid.utils.BottomNavigationUtils;

public class MainActivity extends BaseActivity {

    private BottomNavigationView navigation;
    private IndexFragment indexFragment;
    private SystemCastFragment systemFragment;
    private ProjectFragment projectFragment;
    private MineFragment mineFragment;
    private long exitTime = 0;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        navigation = findViewById(R.id.navigation);
    }

    @Override
    protected void loadData() {
        super.loadData();
        BottomNavigationUtils.disableShiftMode(navigation);
        FragmentManagerUtil.getFragmentManager(this).addFragment(R.id.content_container, new IndexFragment());
        indexFragment = new IndexFragment();
        systemFragment = new SystemCastFragment();
        projectFragment = new ProjectFragment();
        mineFragment = new MineFragment();
    }

    @Override
    protected void initListener() {
        super.initListener();
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_index:
                        FragmentManagerUtil.getFragmentManager(MainActivity.this).addFragment(R.id.content_container, indexFragment);
                        break;
                    case R.id.navigation_system:
                        FragmentManagerUtil.getFragmentManager(MainActivity.this).addFragment(R.id.content_container, systemFragment);
                        break;
                    case R.id.navigation_project:
                        FragmentManagerUtil.getFragmentManager(MainActivity.this).addFragment(R.id.content_container, projectFragment);
                        break;
                    case R.id.navigation_mine:
                        FragmentManagerUtil.getFragmentManager(MainActivity.this).addFragment(R.id.content_container, mineFragment);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (WanAndroidApp.getLoginStatus() == 1) {
            Bundle bundle = new Bundle();
            bundle.putString("username", WanAndroidApp.getUsername());
            mineFragment.setArguments(bundle);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Snackbar.make(navigation, "再按一次退出应用", Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                RetrofitFactory.cookieStore.clear();
                finish();
            }
        }
        return true;
    }
}
