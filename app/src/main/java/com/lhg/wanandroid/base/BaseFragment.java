package com.lhg.wanandroid.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lhgyuyu on 2018/4/25.
 */

public abstract class BaseFragment extends Fragment {
    /**
     * 贴附的activity
     */
    protected Activity mActivity;

    /**
     * 根布局rootView
     */
    protected View rootView;

    /**
     * 是否对用户可见
     */
    private boolean mIsVisible;

    /**
     * 是否加载完成
     * 当执行完onCreateView,View的初始化方法后方法后即为true
     */
    private boolean mIsPrepare;

    public BaseFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            mActivity = getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getFragmentView(), container, false);
        initData(getArguments());
        initView();
        mIsPrepare = true;
        loadData();
        onLazyLoad();
        //EventUtil.register(this);
        initListener();
        return rootView;
    }

    protected void loadData() {

    }

    /**
     * 判断当前是否用户可见
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisible = isVisibleToUser;
        if (mIsPrepare && mIsVisible) {
            /**
             * 用户可见并且加载完成时进行 控件的懒加载
             */
            onLazyLoad();
        }
    }

    /**
     * 返回Fragment的基本布局
     *
     * @return resId
     */
    protected abstract int getFragmentView();

    /**
     * 初始化接收到的arguments
     *
     * @param arguments
     */
    protected void initData(Bundle arguments) {

    }

    /**
     * 懒加载数据方法
     */
    protected void onLazyLoad() {
        if (!mIsPrepare || !mIsVisible) {
            return;
        }
    }

    /**
     * 初始化View
     */
    protected void initView() {

    }

    /**
     * 初始化监听器
     */
    protected void initListener() {

    }

    /**
     * 通过ID查找控件
     *
     * @param viewId 控件资源ID
     * @param <VIEW> 泛型参数，查找得到的View
     * @return 找到了返回控件对象，否则返回null
     */
    public final <VIEW extends View> VIEW findViewById(@IdRes int viewId) {
        return (VIEW) rootView.findViewById(viewId);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param targetActivity 要跳转的目标Activity
     */
    protected void skipActivity(Context context, @NonNull Class<?> targetActivity) {
        startActivity(new Intent(context, targetActivity));
    }

    /**
     * 跳转到指定的Activity
     *
     * @param bundle         要传递的bundle
     * @param targetActivity 要跳转的目标Activity
     */
    protected void skipActivity(@NonNull Class<?> targetActivity, @NonNull Bundle bundle) {
        final Intent intent = new Intent(getContext(), targetActivity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventUtil.unregister(this);
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void getEventBean(BaseEvent event) {
    //
    //    }
}
