package com.lhg.wanandroid.ui.module.mine;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.base.recycler.BaseViewHolder;
import com.lhg.wanandroid.base.recycler.RecyclerViewBaseAdapter;
import com.lhg.wanandroid.bean.CollectWebsiteBean;
import com.lhg.wanandroid.http.RetrofitFactory;
import com.lhg.wanandroid.ui.activity.WebActivity;
import com.lhg.wanandroid.utils.ContantUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CollectWebsiteFragment extends BaseFragment {

    private RecyclerView rv_collect_website;
    private List<CollectWebsiteBean.DataBean> data;
    private RecyclerViewBaseAdapter<CollectWebsiteBean.DataBean> adapter;
    private TextView tv_empty;

    public CollectWebsiteFragment() {

    }

    public static CollectWebsiteFragment newInstance() {
        CollectWebsiteFragment fragment = new CollectWebsiteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_collect_website;

    }

    @Override
    protected void initView() {
        super.initView();
        rv_collect_website = findViewById(R.id.rv_collect_website);
        tv_empty = findViewById(R.id.tv_empty);
        tv_empty.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        super.loadData();
        data = new ArrayList<>();
        rv_collect_website.setLayoutManager(new GridLayoutManager(mActivity, 2, LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewBaseAdapter<CollectWebsiteBean.DataBean>(mActivity, R.layout.item_system, data) {
            @Override
            public void convert(BaseViewHolder holder, CollectWebsiteBean.DataBean dataBean, int position) {
                holder.bindText(R.id.tv_kind_title, data.get(position).getName());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ContantUtils.system_item_back[new Random().nextInt(15)]);
                Palette palette = Palette.from(bitmap).generate();
                holder.getView(R.id.tv_kind_title).setBackgroundColor(palette.getVibrantColor(Color.parseColor("#FFFBB8B8")));
            }
        };
        rv_collect_website.setAdapter(adapter);
        getData();
    }

    private void getData() {
        RetrofitFactory.getInstance().getCollectWebsite(new Observer<CollectWebsiteBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(CollectWebsiteBean collectWebsiteBean) {
                data.addAll(collectWebsiteBean.getData());
                adapter.notifyDataSetChanged();
                tv_empty.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("当前暂时获取不到数据，请点击重试");
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
        tv_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                getData();
            }
        });
    }
}
