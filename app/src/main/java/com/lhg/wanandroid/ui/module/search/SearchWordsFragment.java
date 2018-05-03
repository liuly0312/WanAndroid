package com.lhg.wanandroid.ui.module.search;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;
import com.lhg.wanandroid.base.recycler.BaseViewHolder;
import com.lhg.wanandroid.base.recycler.RecyclerViewBaseAdapter;
import com.lhg.wanandroid.bean.SearchWordsBean;
import com.lhg.wanandroid.http.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SearchWordsFragment extends BaseFragment {


    private RecyclerView rv_search_words;
    private List<SearchWordsBean.DataBean> data;
    private RecyclerViewBaseAdapter<SearchWordsBean.DataBean> adapter;

    public SearchWordsFragment() {
    }

    public static SearchWordsFragment getInstance() {
        return new SearchWordsFragment();
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_search_words;
    }

    @Override
    protected void initView() {
        super.initView();
        rv_search_words = findViewById(R.id.rv_search_words);
    }

    @Override
    protected void loadData() {
        super.loadData();
        data = new ArrayList<>();
        rv_search_words.setLayoutManager(new GridLayoutManager(mActivity, 3, GridLayoutManager.VERTICAL, false));
        adapter = new RecyclerViewBaseAdapter<SearchWordsBean.DataBean>(mActivity, R.layout.item_search_words, data) {
            @Override
            public void convert(BaseViewHolder holder, SearchWordsBean.DataBean dataBean, int position) {
                holder.bindText(R.id.tv_word, dataBean.getName());
            }
        };
        rv_search_words.setAdapter(adapter);
        getData();
    }

    private void getData() {
        RetrofitFactory.getInstance().getSearchWordsBean(new Observer<SearchWordsBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SearchWordsBean searchWordsBean) {
                data.addAll(searchWordsBean.getData());
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
                ((SearchActivity) mActivity).setWords(data.get(position).getName());
            }
        });
    }
}
