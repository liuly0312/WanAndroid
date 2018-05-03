package com.lhg.wanandroid.base.list;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewHolder {

    private Context context;
    public View itemView;
    private SparseArray<View> mViews;

    public ListViewHolder(Context context, View itemView, ViewGroup parent) {
        this.context = context;
        this.itemView = itemView;
        this.mViews = new SparseArray<>();
    }

    public static ListViewHolder getInstance(Context context, ViewGroup parent, View convertView) {
        ListViewHolder listViewHolder = new ListViewHolder(context, convertView, parent);
        return listViewHolder;
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

    public ListViewHolder bindText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ListViewHolder bindImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ListViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
