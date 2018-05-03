package com.lhg.wanandroid.ui.module.search;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.base.recycler.BaseViewHolder;
import com.lhg.wanandroid.base.recycler.RecyclerViewBaseAdapter;
import com.lhg.wanandroid.bean.IndexBean;
import com.lhg.wanandroid.http.RetrofitFactory;
import com.lhg.wanandroid.ui.activity.WebActivity;
import com.lhg.wanandroid.utils.CommonUtils;
import com.lhg.wanandroid.utils.ContantUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SearchResultFragment extends BaseFragment {


    private RecyclerView rv_search_result;
    private List<IndexBean.DataBean.DatasBean> data;
    private RecyclerViewBaseAdapter<IndexBean.DataBean.DatasBean> adapter;
    private int id = 0;
    private String words = "Android";

    public SearchResultFragment() {

    }

    public static SearchResultFragment newInstance() {
        return new SearchResultFragment();
    }

    public void getWords(String words) {
        id = 0;
        this.words = words;
        data.clear();
        getData();
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void initView() {
        super.initView();
        rv_search_result = findViewById(R.id.rv_search_result);
    }

    @Override
    protected void loadData() {
        super.loadData();
        data = new ArrayList<>();
        rv_search_result.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewBaseAdapter<IndexBean.DataBean.DatasBean>(mActivity, R.layout.item_index, data) {
            @Override
            public void convert(BaseViewHolder holder, IndexBean.DataBean.DatasBean dataBean, int position) {
                holder.bindText(R.id.tv_author, dataBean.getAuthor());
                holder.bindText(R.id.tv_title, CommonUtils.handleContainH(dataBean.getTitle()));
                holder.getView(R.id.tv_summary).setVisibility(View.GONE);
                holder.bindText(R.id.tv_summary, !TextUtils.isEmpty(dataBean.getDesc()) ? dataBean.getDesc() : dataBean.getTitle());
                holder.bindText(R.id.tv_date, "发布时间：" + CommonUtils.getFormatDate(dataBean.getPublishTime()));
                Random random = new Random();
                Glide.with(mActivity).load(ContantUtils.authorHead[random.nextInt(ContantUtils.authorHead.length)]).into((ImageView) holder.getView(R.id.iv_logo));
            }
        };
        rv_search_result.setAdapter(adapter);
    }

    private void getData() {
        RetrofitFactory.getInstance().getSearchResultBean(id, this.words, new Observer<IndexBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(IndexBean indexBean) {
                if (indexBean != null) {
                    if (indexBean.getData() != null) {
                        if (indexBean.getData().getDatas().size() > 0) {
                            data.addAll(indexBean.getData().getDatas());
                        }
                    }

                }
                adapter.notifyDataSetChanged();
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
        rv_search_result.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!rv_search_result.canScrollVertically(1) && data.size() > 10) {
                    id++;
                    getData();
                }
            }
        });
    }
}
