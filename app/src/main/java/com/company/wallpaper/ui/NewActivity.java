package com.company.wallpaper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.company.wallpaper.R;
import com.company.wallpaper.adapter.NewListDataAdapter;
import com.company.wallpaper.app.MVVMActivity;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.databinding.ActivityNewBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewActivity extends MVVMActivity<ActivityNewBinding, HomeActivityViewModel> {

    private int pageSize = 10;
    private String typeName;
    private NewListDataAdapter adapter;

    public static void ToNewsListActivity(Context context) {
        context.startActivity(new Intent(context, NewActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        typeName = getIntent().getStringExtra("typeName");
        bindingView.titleBar.getCenterTextView().setText(typeName);
        adapter=new NewListDataAdapter(this,new ArrayList<>());
        bindingView.rc.setAdapter(adapter);
        bindingView.refreshLayout.setOnRefreshListener(refreshLayout -> getData());
        onRefresh();
//        bindingView.refreshLayout.setOnLoadMoreListener(refreshLayout -> getData());
    }

    @Override
    protected void onRefresh() {
        if (adapter != null)
            adapter.clear();
        getData();
    }

    public void getData() {
        mViewModel.news(new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                showContentView();
                bindingView.refreshLayout.finishRefresh(true);
                if (paperTypeBeans.size() < pageSize) {
                    bindingView.refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    bindingView.refreshLayout.finishLoadMore(true);
                }
                adapter.add(paperTypeBeans);
            }

            @Override
            public void onFailed(Throwable throwable) {
                bindingView.refreshLayout.finishRefresh(false);
                bindingView.refreshLayout.finishLoadMore(false);
            }
        });
    }
}
