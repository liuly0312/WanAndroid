package com.lhg.wanandroid.ui.module.search;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private EditText ed_search;
    private TabLayout tab_search;
    private ViewPager vp_search;
    private List<Fragment> data;
    private Toolbar toolbar;
    private SearchResultFragment searchResultFragment;
    private SearchWordsFragment searchWordsFragment;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        super.initView();
        ed_search = findViewById(R.id.ed_search);
        ed_search.setHint("支持多关键字，请用空格分开");
        tab_search = findViewById(R.id.tab_search);
        vp_search = findViewById(R.id.vp_search);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void loadData() {
        super.loadData();
        data = new ArrayList<>();
        searchResultFragment = SearchResultFragment.newInstance();
        searchWordsFragment = SearchWordsFragment.getInstance();
        data.add(searchResultFragment);
        data.add(searchWordsFragment);
        tab_search.addTab(tab_search.newTab().setText("搜索结果"));
        tab_search.addTab(tab_search.newTab().setText("搜索热词"));
        vp_search.setOffscreenPageLimit(2);
        vp_search.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return data.get(position);
            }

            @Override
            public int getCount() {
                return data.size();
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        ed_search.setImeOptions(EditorInfo.IME_ACTION_GO);
        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    setWords(ed_search.getText().toString());
                    return true;
                } else {
                    return false;
                }
            }
        });
        tab_search.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_search));
        vp_search.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_search));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setWords(String str) {
        ed_search.setText(str);
        searchResultFragment.getWords(str);
        vp_search.setCurrentItem(0, true);
    }
}
