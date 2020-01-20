package com.company.wallpaper.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.company.wallpaper.R;
import com.company.wallpaper.adapter.SearchRightAdapter;
import com.company.wallpaper.adapter.SortAdapter;
import com.company.wallpaper.app.BaseFragment;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.databinding.FragmentSearchBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.ui.SearchActivity;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.ysy.commonlib.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2018/8/27.
 */

public class SearchFragment extends BaseFragment<FragmentSearchBinding> implements SortAdapter.RvListener {
    private SortAdapter mSortAdapter;
    private HomeActivityViewModel viewModel;
    private SearchRightAdapter mRightAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLv();
        initView();
        showContentView();
        bindingView.llSearch.setPadding(0, getStateBarHeight() * 3 / 2, 0, 0);
        bindingView.tvSearch.setOnClickListener(v -> startActivity(new Intent(getActivity(), SearchActivity.class)));


    }


    private void initView() {
    }


    private void initLv() {
//        List<String> list=new ArrayList<>();
//        for (int i = 0; i <30 ; i++) {
//            list.add("标题"+i);
//        }
        mSortAdapter = new SortAdapter(getActivity(), new ArrayList<>(), this);
        bindingView.rvSort.setAdapter(mSortAdapter);
        mRightAdapter = new SearchRightAdapter(getActivity(), new ArrayList<>());
        bindingView.rvClassify.setAdapter(mRightAdapter);
        getData();

    }

    private void getData() {
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        viewModel.pageType(new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBean) {
                mSortAdapter.add(paperTypeBean);
                viewModel.papertypesun(paperTypeBean.get(0).getTypeId(), new onResponseListener<List<PaperTypeBean>>() {
                    @Override
                    public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                        mRightAdapter.add(paperTypeBeans);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        DialogUtils.showFailedDialog(getActivity(), bindingView.llSearch, "加载失败，请稍后再试。。。");
                    }
                });
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }


    @Override
    public int setContent() {
        return R.layout.fragment_search;
    }


    @Override
    public void onItemClick(int paperType) {
        mSortAdapter.setCheckedPosition(paperType);
        mRightAdapter.clear();
        viewModel.papertypesun(mSortAdapter.getmDatas().get(paperType).getTypeId(), new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                mRightAdapter.add(paperTypeBeans);
            }

            @Override
            public void onFailed(Throwable throwable) {
//                DialogUtils.showFailedDialog(getActivity(),bindingView.llSearch,"加载失败，请稍后再试。。。");
            }
        });
    }

}
