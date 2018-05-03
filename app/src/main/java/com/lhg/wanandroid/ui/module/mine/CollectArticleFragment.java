package com.lhg.wanandroid.ui.module.mine;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.base.recycler.BaseViewHolder;
import com.lhg.wanandroid.base.recycler.RecyclerViewBaseAdapter;
import com.lhg.wanandroid.bean.CollectArticleBean;
import com.lhg.wanandroid.http.RetrofitFactory;
import com.lhg.wanandroid.ui.activity.WebActivity;
import com.lhg.wanandroid.utils.CommonUtils;
import com.lhg.wanandroid.utils.ContantUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CollectArticleFragment extends BaseFragment {

    private RecyclerView rv_collect_article;
    private List<CollectArticleBean.DataBean.DatasBean> data;
    private RecyclerViewBaseAdapter<CollectArticleBean.DataBean.DatasBean> adapter;
    private int id = 0;
    private TextView tv_empty;

    public CollectArticleFragment() {

    }

    public static CollectArticleFragment newInstance() {
        return new CollectArticleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_collect_article;
    }

    @Override
    protected void initView() {
        super.initView();
        rv_collect_article = findViewById(R.id.rv_collect_article);
        tv_empty = findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        super.loadData();
        data = new ArrayList<>();
        rv_collect_article.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewBaseAdapter<CollectArticleBean.DataBean.DatasBean>(mActivity, R.layout.item_index, data) {

            @Override
            public void convert(BaseViewHolder holder, CollectArticleBean.DataBean.DatasBean dataBean, int position) {
                holder.bindText(R.id.tv_author, dataBean.getAuthor());
                holder.bindText(R.id.tv_title, dataBean.getTitle());
                holder.bindText(R.id.tv_summary, !TextUtils.isEmpty(dataBean.getDesc()) ? dataBean.getDesc() : dataBean.getTitle());
                holder.bindText(R.id.tv_date, "发布时间：" + CommonUtils.getFormatDate(dataBean.getPublishTime()));
                Random random = new Random();
                Glide.with(mActivity).load(ContantUtils.authorHead[random.nextInt(ContantUtils.authorHead.length)]).into((ImageView) holder.getView(R.id.iv_logo));
            }
        };
        rv_collect_article.setAdapter(adapter);
        getData();
    }

    private void getData() {
        RetrofitFactory.getInstance().getCollectArticle(id, new Observer<CollectArticleBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CollectArticleBean collectArticleBean) {
                data.addAll(collectArticleBean.getData().getDatas());
                adapter.notifyDataSetChanged();
                tv_empty.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("当前获取不到数据，点击重试");
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
        rv_collect_article.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && data.size() > 10) {
                    id++;
                    getData();
                }
            }
        });
        tv_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                getData();
            }
        });
    }
}
