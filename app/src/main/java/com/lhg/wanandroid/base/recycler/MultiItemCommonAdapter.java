package com.lhg.wanandroid.base.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import java.util.List;

public abstract class MultiItemCommonAdapter<T> extends RecyclerViewBaseAdapter<T> {

    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public MultiItemCommonAdapter(Context mContext, List<T> mDatas, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(mContext, -1, mDatas);
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        BaseViewHolder holder = BaseViewHolder.get(mContext, parent, layoutId);
        return holder;
    }

}
