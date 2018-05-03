package com.lhg.wanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseActivity;
import com.lhg.wanandroid.base.FragmentManagerUtil;
import com.lhg.wanandroid.bean.SystemCastBean;
import com.lhg.wanandroid.ui.module.mine.AboutFragment;
import com.lhg.wanandroid.ui.module.mine.CollectArticleFragment;
import com.lhg.wanandroid.ui.module.mine.CollectWebsiteFragment;
import com.lhg.wanandroid.ui.module.system.SystemDetailFragment;

/**
 * 二级界面
 */

public class NextActivity extends BaseActivity {

    private Toolbar toolbar;
    private SystemCastBean.DataBean dataBean;
    private String tag = "SystemCast";

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_next;
    }

    @Override
    protected void initView() {
        super.initView();
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void loadData() {
        super.loadData();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            tag = bundle.getString("tag");
            this.dataBean = (SystemCastBean.DataBean) bundle.getSerializable("data");
        }
        switch (tag) {
            case "SystemCast":
                FragmentManagerUtil.getFragmentManager(this).addFragment(R.id.content_container, SystemDetailFragment.getInstance(intent.getExtras()));
                if (dataBean != null) {
                    toolbar.setTitle(dataBean.getName());
                }
                break;
            case "CollectArticle":
                toolbar.setTitle("我的文章收藏");
                FragmentManagerUtil.getFragmentManager(this).addFragment(R.id.content_container, CollectArticleFragment.newInstance());
                break;
            case "CollectWebsite":
                toolbar.setTitle("我的网站收藏");
                FragmentManagerUtil.getFragmentManager(this).addFragment(R.id.content_container, CollectWebsiteFragment.newInstance());
                break;
            case "CollectWords":
                toolbar.setTitle("我的关键字收藏");
                FragmentManagerUtil.getFragmentManager(this).addFragment(R.id.content_container, SystemDetailFragment.getInstance(intent.getExtras()));
                break;
            case "About":
                toolbar.setTitle("关于我们");
                FragmentManagerUtil.getFragmentManager(this).addFragment(R.id.content_container, AboutFragment.newInstance());
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
