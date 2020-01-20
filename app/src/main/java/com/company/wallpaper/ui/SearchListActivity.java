package com.company.wallpaper.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.company.wallpaper.adapter.GridTwoAdapter;
import com.company.wallpaper.app.MVVMActivity;
import com.company.wallpaper.app.MyApplication;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.databinding.ActivitySearchListBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.Dev;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.utils.ad.TTAdManagerHolder;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.ysy.commonlib.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchListActivity extends MVVMActivity<ActivitySearchListBinding, HomeActivityViewModel> {
    private int pageNum = 1;
    private int pageSize = 12;
    private String typeName;
    private GridTwoAdapter adapter;
    private boolean mHasShowDownloadActive;
    private TTAdNative mTTAdNative;

    public static void ToSearchListActivity(String typeName, Context context) {
        Intent intent = new Intent(context, SearchListActivity.class);
        intent.putExtra("typeName", typeName);
        context.startActivity(intent);
    }

    public static void ToSearchListActivity(String typeName, Context context, boolean isSearch) {
        Intent intent = new Intent(context, SearchListActivity.class);
        intent.putExtra("typeName", typeName);
        intent.putExtra("isSearch", isSearch);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        typeName = getIntent().getStringExtra("typeName");
        bindingView.titleBar.getCenterTextView().setText(typeName);
        adapter = new GridTwoAdapter(this, new ArrayList<>());
        bindingView.rc.setAdapter(adapter);
        adapter.setTypeName(typeName);
        bindingView.refreshLayout.setOnRefreshListener(refreshLayout -> {
            SearchListActivity.this.onRefresh();
        });
        if (getIntent().getBooleanExtra("isSearch", false)) {
            bindingView.titleBar.getRightTextView().setVisibility(View.GONE);
        }
        bindingView.setIsSelect(false);
        bindingView.titleBar.getRightTextView().setOnClickListener(v -> {
            bindingView.setIsSelect(!bindingView.getIsSelect());
            bindingView.titleBar.getRightTextView().setText(bindingView.getIsSelect() ? "取消" : "编辑");
            adapter.setSelected(bindingView.getIsSelect());
            adapter.clearSelected();
        });
        adapter.setListener(size -> bindingView.setSelected(size));
        bindingView.refreshLayout.setOnLoadMoreListener(refreshLayout -> getData());
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(MyApplication.getInstance());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (i % 7 == 6 && MyApplication.code == 1) {
                    return 3;
                }
                return 1;
            }
        });
        bindingView.rc.setLayoutManager(layoutManager);
        onRefresh();
        bindingView.btnDeleteAll.setOnClickListener(v -> adapter.selectAll());
        bindingView.btnDelete.setOnClickListener(v -> {
            if (adapter.getCheckList().size() == 0) {
                ToastUtils.showToast("请选择删除的壁纸~");
                return;
            }
            QMUITipDialog qmuiTipDialog = DialogUtils.showLoadingDialog(v.getContext(), "删除中。。。");
            qmuiTipDialog.show();
            if (typeName.equals("我的收藏")) {
                mViewModel.deletecollects(adapter.getPaperId(), adapter.getVideoId(), new onResponseListener() {
                    @Override
                    public void onSuccess(Object o) {
                        onRefresh();
                        qmuiTipDialog.dismiss();
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        qmuiTipDialog.dismiss();
                        ToastUtils.showToast(throwable.getMessage());
                    }
                });
            } else if (typeName.equals("我的足迹")) {
                mViewModel.deletebrowse(adapter.getPaperId(), adapter.getVideoId(), new onResponseListener() {
                    @Override
                    public void onSuccess(Object o) {
                        onRefresh();
                        qmuiTipDialog.dismiss();
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        qmuiTipDialog.dismiss();
                        ToastUtils.showToast(throwable.getMessage());
                    }
                });

            } else if (typeName.equals("我的分享")) {
                mViewModel.deleteshare(adapter.getPaperId(), adapter.getVideoId(), new onResponseListener() {
                    @Override
                    public void onSuccess(Object o) {
                        onRefresh();
                        qmuiTipDialog.dismiss();
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        qmuiTipDialog.dismiss();
                        ToastUtils.showToast(throwable.getMessage());
                    }
                });

            } else if (typeName.equals("我的下载")) {
                mViewModel.deletedownload(adapter.getPaperId(), adapter.getVideoId(), new onResponseListener() {
                    @Override
                    public void onSuccess(Object o) {
                        onRefresh();
                        qmuiTipDialog.dismiss();
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        qmuiTipDialog.dismiss();
                        ToastUtils.showToast(throwable.getMessage());
                    }
                });
            }
        });
    }

    @Override
    protected void onRefresh() {
        pageNum = 1;
        adapter.clear();
        bindingView.refreshLayout.setNoMoreData(false);
        adapter.clearSelected();
        getData();
    }

    public void dealData(List<PaperTypeBean> paperTypeBeans) {
        showContentView();
        bindingView.refreshLayout.finishRefresh(true);
        if (paperTypeBeans.size() < pageSize) {
            bindingView.refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            bindingView.refreshLayout.finishLoadMore(true);
        }
        List<PaperTypeBean> list = new ArrayList<>();
        for (int i = 0; i < paperTypeBeans.size(); i++) {
            list.add(paperTypeBeans.get(i));
            if (i % 6 == 5 && MyApplication.code == 1) {
                PaperTypeBean paperTypeBean = new PaperTypeBean();
                paperTypeBean.setIsAd(true);
                list.add(paperTypeBean);
            }
        }
        adapter.add(list);
    }

    public void dealError(Throwable throwable) {
        showContentView();
//        bindingView.rc.setLayoutManager(new LinearLayoutManager(SearchListActivity.this, LinearLayoutManager.VERTICAL, false));
        bindingView.refreshLayout.finishRefresh();
        bindingView.refreshLayout.finishLoadMore();
    }

    public void getData() {
        if (adapter.getAds().size() <= 2) {
            loadListAd();
        }
        if (typeName.equals("我的收藏")) {
            mViewModel.collectlist(pageNum++, pageSize, new onResponseListener<List<PaperTypeBean>>() {
                @Override
                public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                    dealData(paperTypeBeans);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    dealError(throwable);
                }
            });
        } else if (typeName.equals("我的足迹")) {

            mViewModel.browselist(pageNum++, pageSize, new onResponseListener<List<PaperTypeBean>>() {
                @Override
                public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                    dealData(paperTypeBeans);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    dealError(throwable);
                }
            });

        } else if (typeName.equals("我的分享")) {

            mViewModel.sharelist(pageNum++, pageSize, new onResponseListener<List<PaperTypeBean>>() {
                @Override
                public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                    dealData(paperTypeBeans);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    dealError(throwable);
                }
            });

        } else if (typeName.equals("我的下载")) {
            mViewModel.downloadlist(pageNum++, pageSize, new onResponseListener<List<PaperTypeBean>>() {
                @Override
                public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                    dealData(paperTypeBeans);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    dealError(throwable);
                }
            });
        } else {
            mViewModel.paperquery(pageNum++, pageSize, typeName, new onResponseListener<List<PaperTypeBean>>() {
                @Override
                public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                    dealData(paperTypeBeans);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    dealError(throwable);
                }
            });
        }
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
                .setCodeId("941629078")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setExpressViewAcceptedSize(Dev.px2dp(this, getResources().getDisplayMetrics().widthPixels), 300) //期望模板广告view的size,单位dp
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
                Log.e("addDebug", "msg:" + msg + "code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("addDebug", "SearchAds:onRenderSuccess");
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

