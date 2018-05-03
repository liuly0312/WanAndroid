package com.lhg.wanandroid.base.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class RecyclerViewBaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }

    public RecyclerViewBaseAdapter(Context mContext, int mLayoutId, List<T> mDatas) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.get(mContext, parent, mLayoutId);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, (int) view.getTag());
            }
        });
        holder.itemView.setTag(position);
    }

    public abstract void convert(BaseViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
