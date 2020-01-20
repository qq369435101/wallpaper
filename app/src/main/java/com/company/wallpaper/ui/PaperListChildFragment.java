package com.company.wallpaper.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.company.wallpaper.AppConfig;
import com.company.wallpaper.R;
import com.company.wallpaper.adapter.CommonViewAdapter;
import com.company.wallpaper.adapter.GridTwoAdapter;
import com.company.wallpaper.adapter.VideoAdapter;
import com.company.wallpaper.app.BaseFragment;
import com.company.wallpaper.app.MyApplication;
import com.company.wallpaper.bean.ListPaperBean;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.databinding.FragmentWallpagerChildBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.Dev;
import com.company.wallpaper.utils.ad.TTAdManagerHolder;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//import com.alibaba.android.arouter.launcher.ARouter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaperListChildFragment extends BaseFragment<FragmentWallpagerChildBinding> {
    private int paperType;
    private String paperNmae;
    private HomeActivityViewModel viewModel;
    private int pageNum = 1;
    private int pageSize = 10;
    private CommonViewAdapter adapter;
    private boolean isVideo;
    private List<PaperTypeBean> list;
    private boolean mHasShowDownloadActive;
    private TTAdNative mTTAdNative;

    public PaperListChildFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        String listData = savedInstanceState.getString(paperNmae);
        if (!TextUtils.isEmpty(listData)) {
            ListPaperBean bean = new Gson().fromJson(listData, ListPaperBean.class);
            if (bean != null || bean.getListBeans().size() != 0) {
                list = bean.getListBeans();
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), isVideo ? 1 : 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (i % 10 == 9 && MyApplication.code == 1) {
                    return 3;
                }
                return 1;
            }
        });
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(MyApplication.getInstance());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getActivity());
        bindingView.rc.setLayoutManager(layoutManager);
        if (isVideo) {
            adapter = new VideoAdapter(getActivity(), new ArrayList<>());
        } else {
            adapter = new GridTwoAdapter(getActivity(), new ArrayList<>());
            ((GridTwoAdapter) adapter).setList(true);
        }

        bindingView.rc.setAdapter(adapter);
        getData();
        bindingView.refreshLayout.setOnRefreshListener(refreshLayout -> PaperListChildFragment.this.onRefresh());
        bindingView.refreshLayout.setOnLoadMoreListener(refreshLayout -> getData());
    }

    private void getData() {

        if (adapter instanceof GridTwoAdapter && ((GridTwoAdapter) adapter).getAds().size() <= 2) {
            loadListAd();
        }
        if (list != null && list.size() != 0) {
            adapter.setData(list);
            list = null;
            if (adapter.getDataSize() < AppConfig.DefaultPageSize) {
                bindingView.refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                pageNum = adapter.getDataSize() / AppConfig.DefaultPageSize + 1;
            }
            return;
        }
        if (isVideo) {
            ((VideoAdapter) adapter).setTypeId(paperType);
        } else {
            ((GridTwoAdapter) adapter).setTypeId(paperType);
        }
        viewModel.suncategory(paperType, paperNmae, pageNum++, pageSize, new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                if (paperTypeBeans.size() < AppConfig.DefaultPageSize) {
                    bindingView.refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    bindingView.refreshLayout.finishLoadMore(true);
                }
                bindingView.refreshLayout.finishRefresh(true);
                if (!isVideo) {
                    List<PaperTypeBean> list = new ArrayList<>();
                    for (int i = 0; i < paperTypeBeans.size(); i++) {
                        list.add(paperTypeBeans.get(i));
                        if (i % 9 == 8 && MyApplication.code == 1) {
                            PaperTypeBean paperTypeBean = new PaperTypeBean();
                            paperTypeBean.setIsAd(true);
                            list.add(paperTypeBean);
                        }
                    }
                    adapter.add(list);
                } else {
                    if (adapter != null)
                        adapter.addDatas(paperTypeBeans);
                }
                showContentView();
            }

            @Override
            public void onFailed(Throwable throwable) {
                bindingView.refreshLayout.finishLoadMoreWithNoMoreData();
                bindingView.refreshLayout.finishRefresh(false);
                if (adapter.getDataSize() == 0) {
//                    showError();
                } else {
//                    ToastUtils.showToast("加载失败，请稍后再试");
                }
            }
        });
    }

    public PaperListChildFragment setData(int paperType, String paperName) {
        this.paperType = paperType;
        this.paperNmae = paperName;
        if (paperName.equals("视频")) {
            pageSize = 15;
            isVideo = true;
        } else {
            pageSize = 18;
            isVideo = false;
        }
        return this;
    }

    @Override
    protected void onRefresh() {
        if (adapter != null)
            adapter.clear();
        pageNum = 1;
        bindingView.refreshLayout.setNoMoreData(false);
        getData();
    }

    @Override
    protected void onVisible() {
        if (adapter != null) {
        }
        if (adapter != null && list != null) {
            adapter.setData(list);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ListPaperBean bean = new ListPaperBean();
        bean.setListBeans(adapter.getmDatas());
        outState.putString(paperNmae, new Gson().toJson(bean));
//        outState.putString("pageNum", paperNmae);
        Log.i("onVisible", "paperNmae:" + paperNmae + "onSaveInstanceState");
    }

    @Override
    public int setContent() {
        return R.layout.fragment_wallpager_child;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    boolean canLoad = true;

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
                ((GridTwoAdapter) adapter).addAds(view);
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
