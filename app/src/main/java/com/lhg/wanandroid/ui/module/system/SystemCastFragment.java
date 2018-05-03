package com.lhg.wanandroid.ui.module.system;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.base.list.ListBaseAdapter;
import com.lhg.wanandroid.base.list.ListViewHolder;
import com.lhg.wanandroid.bean.SystemCastBean;
import com.lhg.wanandroid.http.RetrofitFactory;
import com.lhg.wanandroid.ui.activity.NextActivity;
import com.lhg.wanandroid.utils.ContantUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SystemCastFragment extends BaseFragment {


    private GridView gv_system;
    private List<SystemCastBean.DataBean> data;
    private ListBaseAdapter<SystemCastBean.DataBean> adapter;
    private SwipeRefreshLayout swipe;
    private TextView tv_empty;

    public SystemCastFragment() {

    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_system;
    }

    @Override
    protected void initView() {
        super.initView();
        gv_system = findViewById(R.id.gv_system);
        swipe = findViewById(R.id.swipe);
        tv_empty = findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        super.loadData();
        swipe.setColorSchemeResources(R.color.colorPrimary);
        data = new ArrayList<>();
        adapter = new ListBaseAdapter<SystemCastBean.DataBean>(mActivity, data, R.layout.item_system) {
            @Override
            public void bindData(ListViewHolder holder, SystemCastBean.DataBean dataBean, int position) {
                holder.bindText(R.id.tv_kind_title, data.get(position).getName());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ContantUtils.system_item_back[position > 15 ? position - 15 : position]);
                Palette palette = Palette.from(bitmap).generate();
                holder.getView(R.id.tv_kind_title).setBackgroundColor(palette.getVibrantColor(Color.parseColor("#FFFBB8B8")));
            }
        };
        gv_system.setAdapter(adapter);
        getData();
    }

    private void getData() {
        RetrofitFactory.getInstance().getSystemCastBean(new Observer<SystemCastBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SystemCastBean systemBean) {
                data.addAll(systemBean.getData());
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
                tv_empty.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("获取数据失败，点击重试");
                swipe.setRefreshing(false);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                getData();
            }
        });
        adapter.setOnItemClickListener(new ListBaseAdapter.OnItemClickListener() {

            private Intent intent;

            @Override
            public void onClick(View view, int position) {
                intent = new Intent(mActivity, NextActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data.get(position));
                bundle.putSerializable("tag", "SystemCast");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tv_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }
}
