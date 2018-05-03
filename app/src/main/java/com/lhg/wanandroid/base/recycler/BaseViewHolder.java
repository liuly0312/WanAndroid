package com.lhg.wanandroid.base.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    public View itemView;
    private Context mContext;

    public BaseViewHolder(Context mContext, View itemView, ViewGroup parent) {
        super(itemView);
        this.mContext = mContext;
        this.itemView = itemView;
        this.mViews = new SparseArray<>();
    }

    public static BaseViewHolder get(Context context, ViewGroup parent, int layoutId) {

        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        BaseViewHolder holder = new BaseViewHolder(context, itemView, parent);
        return holder;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder bindText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public BaseViewHolder bindImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}

