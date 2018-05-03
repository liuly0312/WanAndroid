package com.lhg.wanandroid.ui.module.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.base.recycler.BaseViewHolder;
import com.lhg.wanandroid.base.recycler.RecyclerViewBaseAdapter;
import com.lhg.wanandroid.bean.IndexBean;
import com.lhg.wanandroid.http.RetrofitFactory;
import com.lhg.wanandroid.ui.activity.WebActivity;
import com.lhg.wanandroid.ui.module.search.SearchActivity;
import com.lhg.wanandroid.utils.CommonUtils;
import com.lhg.wanandroid.utils.ContantUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class IndexFragment extends BaseFragment {

    private int id;
    private List<IndexBean.DataBean.DatasBean> data;
    private RecyclerView rv_index;
    private SwipeRefreshLayout swipe;
    private RecyclerViewBaseAdapter<IndexBean.DataBean.DatasBean> adapter;
    private ImageView iv_search;
    private TextView tv_empty;
    private ImageButton top_back;

    public IndexFragment() {
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_index;
    }

    @Override
    protected void initView() {
        super.initView();
        rv_index = findViewById(R.id.rv_index);
        swipe = findViewById(R.id.swipe);
        iv_search = findViewById(R.id.iv_search);
        tv_empty = findViewById(R.id.tv_empty);
        top_back = findViewById(R.id.top);
    }

    @Override
    protected void loadData() {
        super.loadData();
        id = 0;
        tv_empty.setVisibility(View.VISIBLE);
        data = new ArrayList<>();
        rv_index.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewBaseAdapter<IndexBean.DataBean.DatasBean>(mActivity, R.layout.item_index, data) {
            @Override
            public void convert(BaseViewHolder holder, IndexBean.DataBean.DatasBean dataBean, int position) {
                holder.bindText(R.id.tv_author, dataBean.getAuthor());
                holder.bindText(R.id.tv_title, dataBean.getTitle());
                holder.bindText(R.id.tv_summary, !TextUtils.isEmpty(dataBean.getDesc()) ? dataBean.getDesc() : dataBean.getTitle());
                holder.bindText(R.id.tv_date, "发布时间：" + CommonUtils.getFormatDate(dataBean.getPublishTime()));
                Random random = new Random();
                Glide.with(mActivity).load(ContantUtils.authorHead[random.nextInt(ContantUtils.authorHead.length)]).into((ImageView) holder.getView(R.id.iv_logo));
            }
        };
        swipe.setColorSchemeResources(R.color.colorPrimary);
        rv_index.setAdapter(adapter);
        getData();
    }

    private void getData() {
        RetrofitFactory.getInstance().getIndexBean(id, new Observer<IndexBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(IndexBean indexBean) {
                data.addAll(indexBean.getData().getDatas());
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
        rv_index.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rv_index.canScrollVertically(1)) {
                    id++;
                    getData();
                }
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //搜索按钮
                startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                id = 0;
                data.clear();
                tv_empty.setVisibility(View.VISIBLE);
                getData();
                rv_index.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                }, 5000);
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
                swipe.setRefreshing(true);
                getData();
            }
        });
        top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_index.smoothScrollToPosition(0);
            }
        });
    }
}
