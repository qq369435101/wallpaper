package com.company.wallpaper.ui.home;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.company.wallpaper.AppConfig;
import com.company.wallpaper.R;
import com.company.wallpaper.adapter.HomePageAdapter;
import com.company.wallpaper.app.BaseFragment;
import com.company.wallpaper.app.MyApplication;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.databinding.FragmentHomepageBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.ui.SearchActivity;
import com.company.wallpaper.ui.VideoActivity;
import com.company.wallpaper.utils.Dev;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.utils.ad.TTAdManagerHolder;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.ysy.commonlib.base.TResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends BaseFragment<FragmentHomepageBinding> {

    private HomeActivityViewModel viewModel;
    //    private MyAdapter adapter;
    private HomePageAdapter homePageAdapter;
    private boolean mHasShowDownloadActive;
    boolean frist = true;
    private boolean canLoad = true;

    public HomePageFragment() {
        // Required empty public constructor
    }

    int pageNum = 1;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindingView.llHomepage.setPadding(0, getStateBarHeight() * 3 / 2, 0, 0);
        homePageAdapter = new HomePageAdapter(getActivity(), new ArrayList<>());
//        adapter = new MyAdapter(getActivity(), new ArrayList<>());
        bindingView.recHome.setAdapter(homePageAdapter);
        bindingView.tv1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VideoActivity.class);
            startActivity(intent);
        });
        bindingView.tvSearch.setOnClickListener(v -> startActivity(new Intent(getActivity(), SearchActivity.class)));
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        bindingView.refreshLayout.setOnRefreshListener(refreshLayout -> HomePageFragment.this.onRefresh());
        bindingView.refreshLayout.setOnLoadMoreListener(refreshLayout -> getData());
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(MyApplication.getInstance());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getActivity());
        getData();
        homePageAdapter.setLoadAdListener(this::loadListAd);

    }


    @Override
    public int setContent() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void loadData() {

    }

    private void getData() {
        if (homePageAdapter.getAds().size() <= 2) {
            loadListAd();
        }
        viewModel.HomePageInfo(pageNum++, new onResponseListener<TResponse<List<PaperTypeBean>>>() {
            @Override
            public void onSuccess(TResponse<List<PaperTypeBean>> response) {
                List<PaperTypeBean> homePageBean = response.getData();
                if (homePageBean.size() < AppConfig.DefaultPageSize) {
                    bindingView.refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    bindingView.refreshLayout.finishLoadMore(true);
                }
                bindingView.refreshLayout.finishRefresh(true);
                if (MyApplication.code == 1) {
                    for (int i = 0; i < homePageBean.size(); i++) {
                        if ((i - 3) % 5 == 0) {
                            homePageBean.get(i).setIsAd(true);
                        }
                    }
                }
                homePageAdapter.add(homePageBean);
                showContentView();

            }

            @Override
            public void onFailed(Throwable throwable) {
                bindingView.refreshLayout.finishLoadMore(false);
                bindingView.refreshLayout.finishRefresh(false);
                if (homePageAdapter.getDataSize() == 0) {
                    showError();
                } else {
                    ToastUtils.showToast("加载失败，请稍后再试");
                }
            }

        });
    }


    @Override
    protected void onRefresh() {
        super.onRefresh();
        homePageAdapter.clear();
        pageNum = 1;
        bindingView.refreshLayout.setNoMoreData(false);
        getData();
    }

    private TTAdNative mTTAdNative;

    /**
     * 加载feed广告
     */
    private void loadListAd() {
        if (!canLoad || MyApplication.code != 1) {
            return;
        }
        canLoad = false;
        //feed广告请求类型参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("941629078")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setExpressViewAcceptedSize(Dev.px2dp(getActivity(), getResources().getDisplayMetrics().widthPixels), 300) //期望模板广告view的size,单位dp
                .setAdCount(5)
                .build();
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                canLoad = true;
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                canLoad = true;
//                Log.e("addDebug", "ads:" + ads.size());
                if (ads == null || ads.isEmpty()) {
                    return;
                }
                for (TTNativeExpressAd ad : ads) {
                    bindAdListener(ad);
                    ad.render();
                }
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
            }

            @Override
            public void onAdShow(View view, int type) {
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                homePageAdapter.addAds(view);
                for (int i = 0; i < homePageAdapter.getmDatas().size(); i++) {
                    if ((i - 3) % 5 == 0) {
                        homePageAdapter.notifyItemChanged(i);
                    }
                }

            }
        });

        //dislike设置
//        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
            }

            @Override
            public void onInstalled(String fileName, String appName) {
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
            }
        });
    }

    /**
     * 设置广告的不喜欢，注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
//    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
//        if (customStyle) {
//            //使用自定义样式
//            List<FilterWord> words = ad.getFilterWords();
//            if (words == null || words.isEmpty()) {
//                return;
//            }
//
//            final DislikeDialog dislikeDialog = new DislikeDialog(getActivity(), words);
//            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
//                @Override
//                public void onItemClick(FilterWord filterWord) {
//                    //屏蔽广告
////                    TToast.show(mContext, "点击 " + filterWord.getName());
//                    //用户选择不喜欢原因后，移除广告展示
//                }
//            });
//            ad.setDislikeDialog(dislikeDialog);
//            return;
//        }
//        //使用默认模板中默认dislike弹出样式
//        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
//            @Override
//            public void onSelected(int position, String value) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });
//    }
}
