package com.company.wallpaper.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.company.wallpaper.R;
import com.company.wallpaper.adapter.Tiktok2Adapter;
import com.company.wallpaper.app.MyApplication;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.listener.DownloadListener;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.DownUtils;
import com.company.wallpaper.utils.ShareUtils;
import com.company.wallpaper.utils.TikTokController;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.utils.UIUtils;
import com.company.wallpaper.utils.ad.TTAdManagerHolder;
import com.company.wallpaper.utils.cache.PreloadManager;
import com.company.wallpaper.utils.cache.ProxyVideoCacheManager;
import com.company.wallpaper.view.VerticalViewPager;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.dingmouren.videowallpaper.VideoWallpaper;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysy.commonlib.utils.DialogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class VideoActivity extends BaseActivity implements TikTokController.ControllerListener {
    private boolean canLoad = true;

    public static void ToVideoActivity(Context context, int typeId, int VideoId, int position, String videoUrl) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("typeId", typeId);
        intent.putExtra("VideoId", VideoId);
        intent.putExtra("position", position);
        intent.putExtra("videoUrl", videoUrl);
        context.startActivity(intent);
    }

    private TTAdNative mTTAdNative;
    private HomeActivityViewModel viewModel;
    private int mCurrentPosition;
    private int mPlayingPosition;
    private List<PaperTypeBean> mVideoList;
    private Tiktok2Adapter mTiktok2Adapter;
    private VerticalViewPager mViewPager;

    private PreloadManager mPreloadManager;
    VideoWallpaper mVideoWallpaper;
    /**
     * VerticalViewPager是否反向滑动
     */
    private boolean mIsReverseScroll;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tiktok2;
    }

    @Override
    protected int getTitleResId() {
        return R.string.str_tiktok_2;
    }

    @Override
    protected void initView() {
        super.initView();
        setStatusBarTransparent();
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(getApplicationContext());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
        getData();
    }

    public void getData() {
        loadExpressDrawNativeAd();
        viewModel.checkcollect(getIntent().getIntExtra("typeId", 0), getIntent().getIntExtra("VideoId", 0), new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                List<PaperTypeBean> list = new ArrayList<>();
                for (int i = 0; i < paperTypeBeans.size(); i++) {
                    list.add(paperTypeBeans.get(i));
                    if (i % 8 == 7 && MyApplication.code == 1) {
                        PaperTypeBean paperTypeBean = new PaperTypeBean();
                        paperTypeBean.setIsAd(true);
                        list.add(paperTypeBean);
                    }
                }
                mVideoList = list;
                int position = 0;
                for (PaperTypeBean bean : mVideoList) {
                    if (bean.getVideoUrl() != null && bean.getVideoUrl().equals(getIntent().getStringExtra("videoUrl"))) {
                        position = mVideoList.indexOf(bean);
                        break;
                    }
                }
                initViewPager(position);
                mPreloadManager = PreloadManager.getInstance(VideoActivity.this);
                mVideoWallpaper = new VideoWallpaper();
                VideoWallpaper.setVoiceSilence(VideoActivity.this);
            }

            @Override
            public void onFailed(Throwable throwable) {
                Log.e("okhttp", throwable.getMessage());
                ToastUtils.showToast("服务异常~");
            }
        });
    }

    private void initViewPager(int videoPosition) {
        mViewPager = findViewById(R.id.vvp);
        mViewPager.setOffscreenPageLimit(0);
        mTiktok2Adapter = new Tiktok2Adapter(mVideoList, this);
        mViewPager.setAdapter(mTiktok2Adapter);
        mTiktok2Adapter.setLoadMoreAdsListener(() -> loadExpressDrawNativeAd());
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        if (videoPosition == -1) {
            videoPosition = getIntent().getIntExtra("position", 0);
        }
        mViewPager.setCurrentItem(videoPosition);
        int finalVideoPosition = videoPosition;
        mViewPager.post(() -> startPlay(finalVideoPosition));
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position > mPlayingPosition) {
                    mIsReverseScroll = false;
                } else if (position < mPlayingPosition) {
                    mIsReverseScroll = true;
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
                if (position == mPlayingPosition) return;
                startPlay(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (mCurrentPosition == mPlayingPosition) return;
                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mPreloadManager.resumePreload(mCurrentPosition, mIsReverseScroll);
                } else {
                    mPreloadManager.pausePreload(mCurrentPosition, mIsReverseScroll);
                }
            }
        });
        mTiktok2Adapter.setVideoControllerListener(new Tiktok2Adapter.VideoControllerListener() {
            @Override
            public void onCollect(int videoId, ImageView imageView, boolean isCollect) {
                if (ShareUtils.getUserInfo() == null) {
                    startActivity(new Intent(VideoActivity.this, LoginActivity.class));
                    ToastUtils.showToast("请先登录~");
                    return;
                }
                if (isCollect) {
                    viewModel.deletecollect(0, videoId, new onResponseListener() {
                        @Override
                        public void onSuccess(Object o) {
                            imageView.setImageResource(R.drawable.icon_collect_video_uncheck);
                            ToastUtils.showToast("取消收藏成功~");
                            mTiktok2Adapter.changeCheck(videoId, false);
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            ToastUtils.showToast(throwable.getMessage());
                        }
                    });
                } else {
                    viewModel.collect(0, videoId, new onResponseListener() {
                        @Override
                        public void onSuccess(Object o) {
                            imageView.setImageResource(R.drawable.icon_collect_video);
                            ToastUtils.showToast("收藏成功~");
                            mTiktok2Adapter.changeCheck(videoId, true);
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            ToastUtils.showToast(throwable.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onShare(int videoId) {
                viewModel.share(0, videoId);
            }
        });

    }

    private void startPlay(int position) {
        if (!mTiktok2Adapter.getmVideoBeans().get(position).isAd()) {
            int count = mViewPager.getChildCount();
            for (int i = 0; i < count; i++) {
                View itemView = mViewPager.getChildAt(i);
                Tiktok2Adapter.ViewHolder viewHolder = (Tiktok2Adapter.ViewHolder) itemView.getTag();
                if (viewHolder == null) {
                    continue;
                }
                if (viewHolder.mPosition == position) {
                    VideoView videoView = itemView.findViewById(R.id.video_view);
                    PaperTypeBean tiktokBean = mVideoList.get(position);
                    String playUrl = mPreloadManager.getPlayUrl(tiktokBean.getVideoUrl());
                    videoView.setUrl(playUrl);
                    videoView.start();
                    mPlayingPosition = position;
                    break;
                }
            }
        } else {
            int count = mViewPager.getChildCount();
            mPlayingPosition = position;
            for (int i = 0; i < count; i++) {
                View view = mViewPager.getChildAt(i).findViewById(R.id.video_view);
                if (view != null && view instanceof VideoView) {
                    if (((VideoView) view).isPlaying()) {
                        ((VideoView) view).release();
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoViewManager.instance().pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoViewManager.instance().resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoViewManager.instance().release();
        if (mPreloadManager != null)
            mPreloadManager.removeAllPreloadTask();
        //清除缓存，实际使用可以不需要清除，这里为了方便测试
        ProxyVideoCacheManager.clearAllCache(this);
    }

    public void addData(View view) {
    }

    private void showDialog(boolean isShow) {
        VideoActivity.this.runOnUiThread(() -> {
            findViewById(R.id.progress_wheel).setVisibility(isShow ? View.VISIBLE : View.GONE);
            findViewById(R.id.tv1).setVisibility(isShow ? View.VISIBLE : View.GONE);
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onDown() {
        new RxPermissions(this).request(
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        ).subscribe(granted -> {
            if (!granted) {
                DialogUtils.showFailedDialog(this, mViewPager, "请开启文件权限！");
            } else {
                File file = new File(new File(Environment.getExternalStorageDirectory(), "wallpaperVideo"), "video.mp4");
                QMUITipDialog qmuiTipDialog = DialogUtils.showLoadingDialog(VideoActivity.this, "下载中。。。");
                qmuiTipDialog.show();
                DownUtils.download(mTiktok2Adapter.getmVideoBeans().get(mViewPager.getCurrentItem()).getVideoUrl(), file.getAbsolutePath(), new DownloadListener() {
                    @Override
                    public void onStart() {
                        qmuiTipDialog.dismiss();
                        showDialog(true);
                    }

                    @Override
                    public void onProgress(int progress) {
                        VideoActivity.this.runOnUiThread(() -> {
                            ((ProgressWheel) findViewById(R.id.progress_wheel)).setProgress(progress);
                            ((TextView) findViewById(R.id.tv1)).setText(progress + "%");
                        });
                    }

                    @Override
                    public void onFinish(String path) {
                        qmuiTipDialog.dismiss();
                        Observable.create((ObservableOnSubscribe<String>) e -> {
                            e.onNext(path);
                            e.onComplete();
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                            showDialog(false);
                            mVideoWallpaper.setToWallPaper(VideoActivity.this, s);
                        });
                    }

                    @Override
                    public void onFail(String errorInfo) {
                        qmuiTipDialog.dismiss();
                        DialogUtils.showFailedDialog(VideoActivity.this, mViewPager, "文件下载失败！");
                        showDialog(false);
                    }
                });
            }
        });
    }

    private void loadExpressDrawNativeAd() {
        if (!canLoad) {
            return;
        }
        canLoad = false;
        //step3:创建广告请求参数AdSlot,具体参数含义参考文档
        float expressViewWidth = UIUtils.getScreenWidthDp(this);
        float expressViewHeight = UIUtils.getHeight(this);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("941629106")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setAdCount(3) //请求广告数量为1到3条
                .build();
        //step4:请求广告,对请求回调的广告作渲染处理
        mTTAdNative.loadExpressDrawFeedAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
//                Log.d(TAG, message);
//                showToast(message);
                canLoad = true;
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                canLoad = true;
                if (ads == null || ads.isEmpty()) {
//                    TToast.show(DrawNativeExpressVideoActivity.this, " ad is null!");
                    return;
                }
                for (final TTNativeExpressAd ad : ads) {
                    //点击监听器必须在getAdView之前调
                    ad.setVideoAdListener(new TTNativeExpressAd.ExpressVideoAdListener() {
                        @Override
                        public void onVideoLoad() {

                        }

                        @Override
                        public void onVideoError(int errorCode, int extraCode) {

                        }

                        @Override
                        public void onVideoAdStartPlay() {

                        }

                        @Override
                        public void onVideoAdPaused() {

                        }

                        @Override
                        public void onVideoAdContinuePlay() {

                        }

                        @Override
                        public void onProgressUpdate(long current, long duration) {

                        }

                        @Override
                        public void onVideoAdComplete() {

                        }

                        @Override
                        public void onClickRetry() {
                        }
                    });
                    ad.setCanInterruptVideoPlay(true);
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
                            Log.e("addDebug", "onRenderSuccess");
//                            FrameLayout viewById = (FrameLayout) findViewById(R.id.fm);
//                            viewById.removeAllViews();
//                            viewById.addView(view);
                            mTiktok2Adapter.addAds(view);
                            Log.e("addDebug", "size:" + mTiktok2Adapter.getAds().size());
//                            mTiktok2Adapter.notifyDataSetChanged();
                        }
                    });
                    ad.render();
                }


            }
        });
    }
}
