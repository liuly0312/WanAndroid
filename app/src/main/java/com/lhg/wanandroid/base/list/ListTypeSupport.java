package com.lhg.wanandroid.base.list;

public interface ListTypeSupport<T> {
    /**
     * 返回类型的数量
     */
    int getTypeCount();

    /**
     * 根据数据返回ID
     */
    int getItemType(int position);

    /**
     * 返回单条的布局
     */
    int getLayoutId(int itemType);
}
