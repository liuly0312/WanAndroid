package com.lhg.wanandroid.ui.module.system;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.base.recycler.BaseViewHolder;
import com.lhg.wanandroid.base.recycler.RecyclerViewBaseAdapter;
import com.lhg.wanandroid.bean.SystemListBean;
import com.lhg.wanandroid.http.RetrofitFactory;
import com.lhg.wanandroid.ui.activity.WebActivity;
import com.lhg.wanandroid.utils.CommonUtils;
import com.lhg.wanandroid.utils.ContantUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SystemListFragment extends BaseFragment {

    private RecyclerView rv_system_detail;
    private List<SystemListBean.DataBean.DatasBean> data;
    private RecyclerViewBaseAdapter<SystemListBean.DataBean.DatasBean> adapter;
    private int cid;
    private int id = 0;
    private TextView tv_empty;
    private SwipeRefreshLayout swipe;

    public SystemListFragment() {
    }

    public static SystemListFragment newInstance(Bundle bundle) {
        SystemListFragment fragment = new SystemListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = 0;
            cid = getArguments().getInt("cid");
        }
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_system_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        rv_system_detail = findViewById(R.id.rv_system_detail);
        tv_empty = findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.VISIBLE);
        swipe = findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    protected void loadData() {
        super.loadData();
        data = new ArrayList<>();
        rv_system_detail.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewBaseAdapter<SystemListBean.DataBean.DatasBean>(mActivity, R.layout.item_index, data) {
            @Override
            public void convert(BaseViewHolder holder, SystemListBean.DataBean.DatasBean datasBean, int position) {
                holder.bindText(R.id.tv_author, datasBean.getAuthor());
                holder.bindText(R.id.tv_title, datasBean.getTitle());
                holder.bindText(R.id.tv_summary, !TextUtils.isEmpty(datasBean.getDesc()) ? datasBean.getDesc() : datasBean.getTitle());
                holder.bindText(R.id.tv_date, "发布时间：" + CommonUtils.getFormatDate(datasBean.getPublishTime()));
                Random random = new Random();
                Glide.with(mActivity).load(ContantUtils.authorHead[random.nextInt(ContantUtils.authorHead.length)]).into((ImageView) holder.getView(R.id.iv_logo));
            }
        };
        rv_system_detail.setAdapter(adapter);
        getData();
    }

    public void getData() {
        RetrofitFactory.getInstance().getSystemListBean(id, cid, new Observer<SystemListBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SystemListBean systemChildBean) {
                Log.e("LHG", "数据大小为：" + systemChildBean.getData().getDatas().size());
                data.addAll(systemChildBean.getData().getDatas());
                adapter.notifyDataSetChanged();
                tv_empty.setVisibility(View.GONE);
                swipe.setRefreshing(false);
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
        rv_system_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rv_system_detail.canScrollVertically(1) && data.size() > 10) {
                    id++;
                    getData();
                }
            }
        });
        adapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", data.get(position).getLink());
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
