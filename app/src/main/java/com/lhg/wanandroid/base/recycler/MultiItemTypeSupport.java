package com.lhg.wanandroid.base.recycler;


public interface MultiItemTypeSupport<T> {
    /**
     * 获取布局ID
     *
     * @param itemType
     * @return
     */
    int getLayoutId(int itemType);

    /**
     * 获取单条布局的类型
     *
     * @param position
     * @param t
     * @return
     */
    int getItemViewType(int position, T t);
}
