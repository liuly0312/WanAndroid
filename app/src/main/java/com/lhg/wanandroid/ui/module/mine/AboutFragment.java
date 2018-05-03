package com.lhg.wanandroid.ui.module.mine;

import android.os.Bundle;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseFragment;

public class AboutFragment extends BaseFragment {


    public AboutFragment() {

    }


    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_about;
    }
}
