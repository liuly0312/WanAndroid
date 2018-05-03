package com.lhg.wanandroid.base.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class MultiItemListBaseAdapter<T> extends ListBaseAdapter<T> {

    private ListTypeSupport<T> listTypeSupport;

    public MultiItemListBaseAdapter(Context context, List<T> data, ListTypeSupport<T> listTypeSupport) {
        super(context, data, -1);
        this.listTypeSupport = listTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return listTypeSupport.getItemType(position);
    }

    @Override
    public int getViewTypeCount() {
        return listTypeSupport.getTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(listTypeSupport.getLayoutId(getItemViewType(position)), parent, false);
        ListViewHolder viewHolder = ListViewHolder.getInstance(context, parent, convertView);
        bindData(viewHolder, data.get(position), position);
        return convertView;
    }
}
