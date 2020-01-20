package com.company.wallpaper.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.company.wallpaper.R;
import com.company.wallpaper.adapter.SearchHistoryAdapter;
import com.company.wallpaper.app.MVVMActivity;
import com.company.wallpaper.bean.ListHistoryBean;
import com.company.wallpaper.bean.ListHotBean;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.databinding.ActivitySearchBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.ShareUtils;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends MVVMActivity<ActivitySearchBinding, HomeActivityViewModel> {

    SearchHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initWindow(this);
        changeStateBar(1);
        showContentView();
        //按下搜索按钮时
        bindingView.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                return doSearch();
            }
            return false;
        });
        adapter = new SearchHistoryAdapter(this, new ArrayList<>());
        ListHistoryBean search_history = new Gson().fromJson(ShareUtils.getString("search_history"), ListHistoryBean.class);
        if (search_history != null) {
//            bindingView.setList(new Gson().fromJson(ShareUtils.getString("search_history"), ListHistoryBean.class));
            adapter.add(new Gson().fromJson(ShareUtils.getString("search_history"), ListHistoryBean.class).getList());
            if (adapter.getDataSize() > 0) {
                searchClearCheck();
            }
        } else {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        bindingView.rvHistory.setAdapter(adapter);
        getHot();
    }

    private void searchClearCheck() {
        bindingView.tvClearSearch.setVisibility(View.VISIBLE);
        bindingView.vClear.setVisibility(View.VISIBLE);
        bindingView.tvClearSearch.setOnClickListener(v -> {
            ShareUtils.clearSearch();
            adapter.clear();
            adapter.notifyDataSetChanged();
            bindingView.tvClearSearch.setVisibility(View.GONE);
            bindingView.vClear.setVisibility(View.GONE);
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            ListHistoryBean search_history = new Gson().fromJson(ShareUtils.getString("search_history"), ListHistoryBean.class);
            if (search_history != null) {
//            bindingView.setList(new Gson().fromJson(ShareUtils.getString("search_history"), ListHistoryBean.class));
                adapter.add(new Gson().fromJson(ShareUtils.getString("search_history"), ListHistoryBean.class).getList());
                if (adapter.getDataSize() > 0) {
                    searchClearCheck();
                }
            } else {
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean doSearch() {
        if (!bindingView.etSearch.getText().toString().trim().equals("")) {
            ShareUtils.putSearch(bindingView.etSearch.getText().toString().trim());
            adapter.add(bindingView.etSearch.getText().toString().trim());
            SearchListActivity.ToSearchListActivity(bindingView.etSearch.getText().toString().trim(), this,true);
            bindingView.etSearch.setText("");
            searchClearCheck();
            return true;
        } else {

            return false;
        }
    }


    private void getHot() {
        mViewModel.typehot(new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                bindingView.setList(new ListHotBean(paperTypeBeans));
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }

}
