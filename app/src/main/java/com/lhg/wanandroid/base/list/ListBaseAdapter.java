package com.lhg.wanandroid.base.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


public abstract class ListBaseAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> data;
    protected int layoutId;
    protected OnItemClickListener onItemClickListener;

    public ListBaseAdapter(Context context, List<T> data, int layoutId) {
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ListViewHolder holder = ListViewHolder.getInstance(context, parent, convertView);
        bindData(holder, data.get(position), position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(view, position);
            }
        });
        return convertView;
    }

    public abstract void bindData(ListViewHolder holder, T t, int position);
}
