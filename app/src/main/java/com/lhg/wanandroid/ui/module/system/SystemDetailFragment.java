package com.lhg.wanandroid.ui.module.system;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.bean.SystemCastBean;

import java.util.ArrayList;
import java.util.List;

public class SystemDetailFragment extends BaseFragment {

    private ViewPager vp_project;
    private TabLayout tab_project;
    private List<Fragment> fragments;
    private SystemCastBean.DataBean dataBean;
    private SwipeRefreshLayout swipe;

    public SystemDetailFragment() {

    }

    public static SystemDetailFragment getInstance(Bundle bundle) {
        SystemDetailFragment systemChildFragment = new SystemDetailFragment();
        systemChildFragment.setArguments(bundle);
        return systemChildFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.dataBean = (SystemCastBean.DataBean) bundle.getSerializable("data");
        }
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_system_child;
    }

    @Override
    protected void initView() {
        super.initView();
        tab_project = findViewById(R.id.tab_common);
        vp_project = findViewById(R.id.vp_common);
    }

    @Override
    protected void loadData() {
        super.loadData();
        fragments = new ArrayList<>();
        for (int i = 0; i < dataBean.getChildren().size(); i++) {
            tab_project.addTab(tab_project.newTab().setText(dataBean.getChildren().get(i).getName()));
            Bundle bundle = new Bundle();
            bundle.putInt("cid", dataBean.getChildren().get(i).getId());
            fragments.add(SystemListFragment.newInstance(bundle));
        }
        vp_project.setOffscreenPageLimit(5);
        vp_project.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        vp_project.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_project));
        tab_project.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_project));
    }
}
