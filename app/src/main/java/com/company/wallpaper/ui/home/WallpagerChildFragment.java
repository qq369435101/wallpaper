package com.company.wallpaper.ui.home;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.company.wallpaper.R;
import com.company.wallpaper.adapter.VideoListAdapter;
import com.company.wallpaper.app.BaseFragment;
import com.company.wallpaper.app.MyApplication;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.databinding.FragmentWallpagerChildBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.Dev;
import com.company.wallpaper.utils.ad.TTAdManagerHolder;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WallpagerChildFragment extends BaseFragment<FragmentWallpagerChildBinding> {
    private VideoListAdapter adapter;
    private int typeId;
    private HomeActivityViewModel viewModel;
    private int pageNum = 1;
    private boolean mHasShowDownloadActive;
    private TTAdNative mTTAdNative;

    public WallpagerChildFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();

        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        adapter = new VideoListAdapter(getActivity(), new ArrayList<>(), typeId);
        bindingView.rc.setAdapter(adapter);
        bindingView.refreshLayout.setOnRefreshListener(refreshLayout -> WallpagerChildFragment.this.onRefresh());
        bindingView.refreshLayout.setOnLoadMoreListener(refreshLayout -> getData());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (i % 9 == 8&&MyApplication.code==1) {
                    return 2;
                }
                return 1;
            }
        });
        bindingView.rc.setLayoutManager(layoutManager);
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(MyApplication.getInstance());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getActivity());
        onRefresh();
    }

    private void getData() {
        if (adapter.getAds().size() <= 2) {
            loadListAd();
        }
        viewModel.video(typeId, pageNum++, new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                showContentView();
                bindingView.refreshLayout.finishRefresh(true);
                if (paperTypeBeans.size() < 16) {
                    bindingView.refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    bindingView.refreshLayout.finishLoadMore(true);
                }

                List<PaperTypeBean> list = new ArrayList<>();
                for (int i = 0; i < paperTypeBeans.size(); i++) {
                    list.add(paperTypeBeans.get(i));
                    if (i % 8 == 7 && MyApplication.code == 1) {
                        PaperTypeBean paperTypeBean = new PaperTypeBean();
                        paperTypeBean.setIsAd(true);
                        list.add(paperTypeBean);
                    }
                }
                adapter.add(list);
            }

            @Override
            public void onFailed(Throwable throwable) {
                showContentView();
                bindingView.refreshLayout.finishRefresh(false);
                bindingView.refreshLayout.finishLoadMore(false);
            }
        });
    }


    @Override
    public int setContent() {
        return R.layout.fragment_wallpager_child;
    }

    public WallpagerChildFragment setTypeId(int typeId) {
        this.typeId = typeId;
        return this;
    }

    @Override
    protected void onRefresh() {
        pageNum = 1;
        adapter.clear();
        bindingView.refreshLayout.setNoMoreData(false);

        getData();
    }

    boolean canLoad = true;

    /**
     * 加载feed广告
     */
    private void loadListAd() {
        if (!canLoad) {
            return;
        }
        canLoad = false;
        //feed广告请求类型参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("941629083")
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
                Log.e("addDebug", "ads:" + ads.size());
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
                adapter.addAds(view);
                adapter.notifyDataSetChanged();

            }
        });

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
}
