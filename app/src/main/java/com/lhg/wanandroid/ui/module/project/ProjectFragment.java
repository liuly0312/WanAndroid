package com.lhg.wanandroid.ui.module.project;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.bean.ProjectCastBean;
import com.lhg.wanandroid.http.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ProjectFragment extends BaseFragment {


    private TabLayout tab_project;
    private ViewPager vp_project;
    private List<Fragment> fragments;
    private List<ProjectCastBean.DataBean> data;
    private FragmentStatePagerAdapter adapter;

    public ProjectFragment() {
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_project;
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
        getData();
        fragments = new ArrayList<>();
        data = new ArrayList<>();
        vp_project.setOffscreenPageLimit(5);
        adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        vp_project.setAdapter(adapter);
    }

    private void getData() {
        RetrofitFactory.getInstance().getProjectCastBean(new Observer<ProjectCastBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ProjectCastBean projectCastBean) {
                data.addAll(projectCastBean.getData());
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < data.size(); i++) {
                            tab_project.addTab(tab_project.newTab().setText(data.get(i).getName()));
                            Bundle bundle = new Bundle();
                            bundle.putInt("cid", data.get(i).getId());
                            fragments.add(ProjectListFragment.newInstance(bundle));
                        }
                        adapter.notifyDataSetChanged();
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

    @Override
    protected void initListener() {
        super.initListener();
        vp_project.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_project));
        tab_project.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_project));
    }
}
