package com.lhg.wanandroid.ui.module.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.base.recycler.BaseViewHolder;
import com.lhg.wanandroid.base.recycler.RecyclerViewBaseAdapter;
import com.lhg.wanandroid.bean.ProjectListBean;
import com.lhg.wanandroid.http.RetrofitFactory;
import com.lhg.wanandroid.ui.activity.WebActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ProjectListFragment extends BaseFragment {


    private RecyclerView rv_project;
    private List<ProjectListBean.DataBean.DatasBean> data;
    private int id;
    private int cid;
    private RecyclerViewBaseAdapter<ProjectListBean.DataBean.DatasBean> adapter;
    private SwipeRefreshLayout swipe;
    private SwipeRefreshLayout swipe1;
    private TextView empty;

    public ProjectListFragment() {
    }


    public static ProjectListFragment newInstance(Bundle bundle) {
        ProjectListFragment fragment = new ProjectListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            cid = bundle.getInt("cid");
        }
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_project_list;
    }

    @Override
    protected void initView() {
        super.initView();
        rv_project = findViewById(R.id.rv_project);
        swipe = findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.colorPrimary);
        empty = findViewById(R.id.tv_empty);
        empty.setVisibility(View.VISIBLE);
    }

    private void getData() {
        RetrofitFactory.getInstance().getProjectListBean(id, cid, new Observer<ProjectListBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ProjectListBean projectListBean) {
                data.addAll(projectListBean.getData().getDatas());
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
                empty.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                empty.setText("数据获取失败，请点击重试");
                empty.setVisibility(View.VISIBLE);
                swipe.setRefreshing(false);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void loadData() {
        super.loadData();
        data = new ArrayList<>();
        rv_project.setLayoutManager(new GridLayoutManager(mActivity, 2, LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewBaseAdapter<ProjectListBean.DataBean.DatasBean>(mActivity, R.layout.item_project, data) {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void convert(BaseViewHolder holder, ProjectListBean.DataBean.DatasBean dataBean, int position) {
                holder.bindText(R.id.tv_title, dataBean.getTitle());
                holder.bindText(R.id.tv_content, dataBean.getDesc());
                holder.bindText(R.id.tv_date, new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(dataBean.getPublishTime()));
                holder.bindText(R.id.tv_author, dataBean.getAuthor());
                Glide.with(mActivity).load(dataBean.getEnvelopePic()).into((ImageView) holder.getView(R.id.iv_back));
            }
        };
        rv_project.setAdapter(adapter);
        getData();
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
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                getData();
            }
        });
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }
}
