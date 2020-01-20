package com.company.wallpaper.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.company.wallpaper.AppConfig;
import com.company.wallpaper.R;
import com.company.wallpaper.app.MVVMActivity;
import com.company.wallpaper.app.MyApplication;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.databinding.ActivityPhotoBrowserBinding;
import com.company.wallpaper.databinding.LayoutViewBroswBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.Dev;
import com.company.wallpaper.utils.FileUtils;
import com.company.wallpaper.utils.GlideCacheUtil;
import com.company.wallpaper.utils.ShareUtils;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.utils.ad.TTAdManagerHolder;
import com.company.wallpaper.utils.wechatutils.WechatLoginHelper;
import com.company.wallpaper.view.CustomDialog;
import com.company.wallpaper.view.addressdialog.DislikeDialog;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.ysy.commonlib.utils.DialogUtils;
import com.ysy.commonlib.utils.StringUtils;
import com.ysy.commonlib.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ImageShowActivity extends MVVMActivity<ActivityPhotoBrowserBinding, HomeActivityViewModel> {
    private ViewPager mPager;
    //    private ImageView centerIv;
//    private TextView photoOrderTv;
    private ArrayList<PaperTypeBean> imageUrls = new ArrayList();
    private int curPosition = -1;
    private int[] initialedPositions = null;
    private ObjectAnimator objectAnimator;
    private View curPage;
    CustomDialog<LayoutViewBroswBinding> bottom_dialog;
    private boolean hasData;
    private int paperId;
    private TTAdNative mTTAdNative;
    private FrameLayout mExpressContainer;
    private TTNativeExpressAd mTTAd;
    private boolean justSave = false;

    public static void toImageViewActivity(Context context, int PaperId, int TypeId, int VideoId) {
        Intent intent = new Intent(context, ImageShowActivity.class);
        intent.putExtra("PaperId", PaperId);
        intent.putExtra("TypeId", TypeId);
        intent.putExtra("VideoId", VideoId);
        context.startActivity(intent);
    }

    public static void toImageViewActivity(Context context, int PaperId, String fromType, String typeName, int VideoId, int TypeId, boolean isList) {
        Intent intent = new Intent(context, ImageShowActivity.class);
        intent.putExtra("PaperId", PaperId);
        intent.putExtra("VideoId", VideoId);
        intent.putExtra("fromType", fromType);
        intent.putExtra("typeName", typeName);
        intent.putExtra("TypeId", TypeId);
        intent.putExtra("isList", isList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow(this);
        Utils.setStatusTextColor(false, this);
        changeBackMode();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo_browser);
        bindingView.expressContainer.setVisibility(MyApplication.code == 1 ? View.VISIBLE : View.GONE);
        paperId = getIntent().getIntExtra("PaperId", 0);
        if (TextUtils.isEmpty(getIntent().getStringExtra("fromType"))) {
            getData();
        } else {
            if (getIntent().getBooleanExtra("isList", false)) {
                getData3();
            } else
                getData2();
        }
        mExpressContainer = bindingView.expressContainer;
        bindingView.tvBack.setOnClickListener(v -> onBackPressed());
        mPager = findViewById(R.id.pager);
        bindingView.tvDown.setOnClickListener(v -> {
            if (!hasData) {
                return;
            }
            mViewModel.download(
                    imageUrls.get(mPager.getCurrentItem()).getPaperId(), new onResponseListener<String>() {
                        @Override
                        public void onSuccess(String s) {

                        }

                        @Override
                        public void onFailed(Throwable throwable) {

                        }
                    });
            RxPermissions permissions = new RxPermissions(ImageShowActivity.this);
            permissions.request(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                if (!aBoolean) {
                    QMUITipDialog qmuiTipDialog = DialogUtils.showLoadingDialog(v.getContext(), "保存中。。。");
                    qmuiTipDialog.show();
//                    Log.e("okhttp","position:"+bindingView.pager.getCurrentItem()+"--pagerSize:"+bindingView.pager.getChildCount());
                    curPage.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(curPage.getDrawingCache());
                    new Thread(() -> {
                        FileUtils.saveToSystemGallery(bitmap, ImageShowActivity.this, new FileUtils.SaveResultCallback() {
                            @Override
                            public void onSavedSuccess() {
                                v.post(() -> {
                                    qmuiTipDialog.dismiss();
                                    DialogUtils.showSuccessDialog(v.getContext(), v, "图片保存成功！");
                                });
                            }

                            @Override
                            public void onSavedFailed() {
                                v.post(() -> {
                                    qmuiTipDialog.dismiss();
                                    DialogUtils.showFailedDialog(v.getContext(), v, "图片保存失败！");
                                });
                            }
                        });
                        curPage.setDrawingCacheEnabled(false);
                    }).start();
                } else {
                    ToastUtils.showToast("无保存权限！");
                }
            });


        });
        bindingView.tvCollect.setOnClickListener(v -> {
            if (!hasData) {
                return;
            }
            if (ShareUtils.getUserInfo() == null) {
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
            if (imageUrls != null && mPager.getCurrentItem() < imageUrls.size()) {
                doCollect(imageUrls.get(mPager.getCurrentItem()).isFlg(), imageUrls.get(mPager.getCurrentItem()).getPaperId());
            }
        });
        showContentView();
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);
        loadExpressAd("941629096");
    }

    public void doCollect(boolean isFlg, int paperId) {
        if (!hasData) {
            return;
        }
        if (!isFlg) {
            mViewModel.collect(paperId, 0, new onResponseListener() {
                @Override
                public void onSuccess(Object s) {
                    DialogUtils.showSuccessDialog(ImageShowActivity.this, bindingView.ivCollect, "收藏成功");
                    bindingView.ivCollect.setImageResource(R.drawable.icon_collect_img);
                    imageUrls.get(mPager.getCurrentItem()).setFlg(true);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    DialogUtils.showFailedDialog(ImageShowActivity.this, bindingView.ivCollect, throwable.getMessage());
                }
            });
        } else {
            mViewModel.deletecollect(paperId, 0, new onResponseListener() {
                @Override
                public void onSuccess(Object s) {
                    DialogUtils.showSuccessDialog(ImageShowActivity.this, bindingView.ivCollect, "取消收藏成功");
                    bindingView.ivCollect.setImageResource(R.drawable.icon_uncollect_img);
                    imageUrls.get(mPager.getCurrentItem()).setFlg(false);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    DialogUtils.showFailedDialog(ImageShowActivity.this, bindingView.ivCollect, throwable.getMessage());
                }
            });

        }

    }

    private void initView() {

        bindingView.tvShare.setOnClickListener(v -> {
            final int TAG_SHARE_WECHAT_FRIEND = 0;
            final int TAG_SHARE_WECHAT_MOMENT = 1;
            QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(ImageShowActivity.this);
            builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                    .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                    .setOnSheetItemClickListener((dialog, itemView) -> {
                        mViewModel.share(imageUrls.get(mPager.getCurrentItem()).getPaperId(), 0);
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
//                                ToastUtils.showToast("分享到微信");
                                WechatShare(ImageShowActivity.this, AppConfig.ShareUrl + "?paperId=" + paperId, "超高清壁纸", "我推荐一张壁纸给你", true);
                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
//                                ToastUtils.showToast("分享到朋友圈");
                                WechatShare(ImageShowActivity.this, AppConfig.ShareUrl + "?paperId=" + paperId, "超高清壁纸", "我推荐一张壁纸给你", false);
                                break;
                        }
                    }).build().show();
        });
        bindingView.tvBrowse.setOnClickListener(v -> {
            if (!hasData) {
                return;
            }
            if (bottom_dialog == null) {
                bottom_dialog = new CustomDialog<>(ImageShowActivity.this, R.layout.layout_view_brosw, true);
                bottom_dialog.getBindView().tvLock.setOnClickListener(v1 -> {
                    bindingView.setType(1);
                    bottom_dialog.dismiss();
                });
                bottom_dialog.getBindView().tvMainScreen.setOnClickListener(v12 -> {
                    bindingView.setType(2);
                    bottom_dialog.dismiss();
                });
            }

            if (!bottom_dialog.isShowing())
                bottom_dialog.show();
        });
//        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));


        bindingView.setType(1);
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageUrls.size();
            }


            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                if (imageUrls.get(position) != null && !"".equals(imageUrls.get(position))) {
                    final ImageView view = new ImageView(ImageShowActivity.this);
                    view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(ImageShowActivity.this).load(imageUrls.get(position).getPaperUrl()).apply(new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).fitCenter()).transition(DrawableTransitionOptions.withCrossFade()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (position == curPosition) {
                                hideLoadingAnimation();
                            }
                            showErrorLoading();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (position == curPosition) {
                                hideLoadingAnimation();
                            }
                            return false;
                        }

                    }).into(view);
                    view.setOnClickListener(v -> {
                        bindingView.llBottom.setVisibility(bindingView.llBottom.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                    });
                    container.addView(view);
                    return view;
                }
                return null;
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                releaseOnePosition(position);
                container.removeView((View) object);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                curPage = (View) object;
            }
        });
        curPosition = returnClickedPosition() == -1 ? 0 : returnClickedPosition();
        mPager.setCurrentItem(curPosition);
        mPager.setTag(curPosition);
        if (initialedPositions[curPosition] != curPosition) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
            showLoadingAnimation();
        }

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (initialedPositions[position] != position) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
                    showLoadingAnimation();
                } else {
                    hideLoadingAnimation();
                }
                curPosition = position;
                mPager.setTag(position);//为当前view设置tag
                bindingView.ivCollect.setImageResource(imageUrls.get(position).isFlg() ? R.drawable.icon_collect_img : R.drawable.icon_uncollect_img);
                paperId = imageUrls.get(mPager.getCurrentItem()).getPaperId();
                if (TextUtils.isEmpty(getIntent().getStringExtra("fromType"))) {
                    getData();
                } else {
                    if (getIntent().getBooleanExtra("isList", false)) {
                        getData3();
                    } else
                        getData2();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("Change", "onPageScrollStateChanged:" + state);
            }
        });
        Date dd = new Date();
        ((TextView) findViewById(R.id.tv_time)).setText(new SimpleDateFormat("HH:mm").format(dd));
        ((TextView) findViewById(R.id.tv_md)).setText(new SimpleDateFormat("MM月dd日  ").format(dd) + getWeekOfDate(dd));

    }

    public String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    private void checkcollect(int position) {
        mViewModel.checkcollect(paperId, getIntent().getIntExtra("TypeId", 0), getIntent().getIntExtra("VideoId", 0), new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                if (imageUrls.get(position).isFlg()) {
                    bindingView.ivCollect.setImageResource(R.drawable.icon_collect_img);
                } else {
                    bindingView.ivCollect.setImageResource(R.drawable.icon_uncollect_img);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
            }
        });
    }

    private void getData() {
        mViewModel.checkcollect(paperId, getIntent().getIntExtra("TypeId", 0), getIntent().getIntExtra("VideoId", 0), new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                if (justSave) {
                    return;
                }
                justSave = true;
                hasData = true;
                imageUrls.addAll(paperTypeBeans);
                initialedPositions = new int[imageUrls.size()];
                initInitialedPositions();
                initView();
                for (PaperTypeBean imageUrl : imageUrls) {
                    if (imageUrl.getPaperId() == paperId) {
                        bindingView.tvClassify.setText(imageUrl.getTypeName());
                        GlideCacheUtil.LoadImage(ImageShowActivity.this, bindingView.ivPaper, imageUrl.getPaperUrl());
                        bindingView.llClassify.setOnClickListener(v -> PaperListActivity.toPagerListActivity(imageUrl.getTypeId(), imageUrl.getTypeName(), 1, ImageShowActivity.this));
                        if (imageUrl.isFlg()) {
                            bindingView.ivCollect.setImageResource(R.drawable.icon_collect_img);
                        } else {
                            bindingView.ivCollect.setImageResource(R.drawable.icon_uncollect_img);
                        }
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                ToastUtils.showToast("加载失败~");
            }
        });
    }

    private void getData2() {
        mViewModel.checkcollect(getIntent().getStringExtra("typeName"), getIntent().getIntExtra("PaperId", 0), getIntent().getIntExtra("VideoId", 0), getIntent().getIntExtra("TypeId", 0), new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                if (justSave) {
                    return;
                }
                justSave = true;
                hasData = true;
                imageUrls.addAll(paperTypeBeans);
                initialedPositions = new int[imageUrls.size()];
                initInitialedPositions();
                initView();
                for (PaperTypeBean imageUrl : imageUrls) {
                    if (imageUrl.getPaperId() == paperId) {
                        bindingView.tvClassify.setText(imageUrl.getTypeName());
                        GlideCacheUtil.LoadImage(ImageShowActivity.this, bindingView.ivPaper, imageUrl.getPaperUrl());
                        bindingView.llClassify.setOnClickListener(v -> PaperListActivity.toPagerListActivity(imageUrl.getTypeId(), imageUrl.getTypeName(), 1, ImageShowActivity.this));
                        if (imageUrl.isFlg()) {
                            bindingView.ivCollect.setImageResource(R.drawable.icon_collect_img);
                        } else {
                            bindingView.ivCollect.setImageResource(R.drawable.icon_uncollect_img);
                        }
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                ToastUtils.showToast("加载失败~");
            }
        });
    }

    private void getData3() {
        mViewModel.checkcollect(getIntent().getIntExtra("PaperId", 0), getIntent().getStringExtra("fromType"), getIntent().getIntExtra("VideoId", 0), getIntent().getIntExtra("TypeId", 0), new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                if (justSave) {
                    return;
                }
                justSave = true;
                hasData = true;
                imageUrls.addAll(paperTypeBeans);
                initialedPositions = new int[imageUrls.size()];
                initInitialedPositions();
                initView();
                for (PaperTypeBean imageUrl : imageUrls) {
                    if (imageUrl.getPaperId() == paperId) {
                        bindingView.tvClassify.setText(imageUrl.getTypeName());
                        GlideCacheUtil.LoadImage(ImageShowActivity.this, bindingView.ivPaper, imageUrl.getPaperUrl());
                        bindingView.llClassify.setOnClickListener(v -> PaperListActivity.toPagerListActivity(imageUrl.getTypeId(), imageUrl.getTypeName(), 1, ImageShowActivity.this));
                        if (imageUrl.isFlg()) {
                            bindingView.ivCollect.setImageResource(R.drawable.icon_collect_img);
                        } else {
                            bindingView.ivCollect.setImageResource(R.drawable.icon_uncollect_img);
                        }
                    }
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                ToastUtils.showToast("加载失败~");
            }
        });
    }

    private int returnClickedPosition() {
        if (imageUrls == null) {
            return -1;
        }
        for (int i = 0; i < imageUrls.size(); i++) {
            if (paperId == imageUrls.get(i).getPaperId()) {
                return i;
            }
        }
        return -1;
    }


    private void showLoadingAnimation() {
//        centerIv.setVisibility(View.VISIBLE);
//        centerIv.setImageResource(R.drawable.loading);
//        if (objectAnimator == null) {
//            objectAnimator = ObjectAnimator.ofFloat(centerIv, "rotation", 0f, 360f);
//            objectAnimator.setDuration(2000);
//            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                objectAnimator.setAutoCancel(true);
//            }
//        }
//        objectAnimator.start();
    }

    private void hideLoadingAnimation() {
        releaseResource();
//        centerIv.setVisibility(View.GONE);
    }

    private void showErrorLoading() {
//        centerIv.setVisibility(View.VISIBLE);
        releaseResource();
//        centerIv.setImageResource(R.drawable.load_error);
    }

    private void releaseResource() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
//        if (centerIv.getAnimation() != null) {
//            centerIv.getAnimation().cancel();
//        }
    }

    private void occupyOnePosition(int position) {
        initialedPositions[position] = position;
    }

    private void releaseOnePosition(int position) {
        initialedPositions[position] = -1;
    }

    private void initInitialedPositions() {
        for (int i = 0; i < initialedPositions.length; i++) {
            initialedPositions[i] = -1;
        }
    }

    @Override
    protected boolean isChangeStateBar() {
        return false;
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return false;
    }

    @Override
    protected void onDestroy() {
        releaseResource();
        curPage = null;
        if (mPager != null) {
            mPager.removeAllViews();
            mPager = null;
        }
        super.onDestroy();
    }

    private void WechatShare(Context context, String webUrl, String title, String desc, boolean isFriend) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
//        Bitmap thumb = null;
//        try {
//            thumb = BitmapFactory.decodeStream(new URL(imageUrls.get(bindingView.pager.getCurrentItem()).getPaperUrl()).openStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb,120,150,true);
//        thumb.recycle();
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = StringUtils.buildTransaction("webpage");
        req.message = msg;
//        msg.thumbData=FileUtils.bitmap2Bytes(thumbBmp,32);
        if (isFriend) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        WechatLoginHelper.doShare(context, req);
//        bitmap.recycle();
    }

    private void loadExpressAd(String codeId) {
        if (MyApplication.code != 1) {
            return;
        }
        mExpressContainer.removeAllViews();
        float expressViewWidth = Dev.px2dp(this, getResources().getDisplayMetrics().widthPixels);
        float expressViewHeight = 60;
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
//                TToast.show(BannerExpressActivity.this, "load error : " + code + ", " + message);
                mExpressContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
//                mTTAd.setSlideIntervalTime(30*1000);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
            }
        });
    }

    private boolean mHasShowDownloadActive = false;
    private long startTime = 0;

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
//                TToast.show(mContext, "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
//                TToast.show(mContext, "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
//                Log.e("ExpressView","render fail:"+(System.currentTimeMillis() - startTime));
//                TToast.show(mContext, msg+" code:"+code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
//                Log.e("ExpressView","render suc:"+(System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
//                TToast.show(mContext, "渲染成功");
                mExpressContainer.removeAllViews();
                mExpressContainer.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
//                TToast.show(BannerExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
//                    TToast.show(BannerExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(BannerExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(this, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
//                    TToast.show(mContext, "点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    mExpressContainer.removeAllViews();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(ImageShowActivity.this, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
//                TToast.show(mContext, "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                mExpressContainer.removeAllViews();
            }

            @Override
            public void onCancel() {
//                TToast.show(mContext, "点击取消 ");
            }
        });
    }
}
