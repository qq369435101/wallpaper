package com.company.wallpaper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.company.wallpaper.AppConfig;
import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.utils.TikTokController;
import com.company.wallpaper.utils.cache.PreloadManager;
import com.dueeeke.videoplayer.player.VideoView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;

import static com.company.wallpaper.utils.wechatutils.WechatLoginHelper.WechatShare;

public class Tiktok2Adapter extends PagerAdapter {
    private List<View> ads = new ArrayList<>();

    public void addAds(View view) {
        ads.add(view);
    }

    public List<View> getAds() {
        return ads;
    }

    public void setAds(List<View> ads) {
        this.ads = ads;
    }

    /**
     * View缓存池，从ViewPager中移除的item将会存到这里面，用来复用
     */
    private List<View> mViewPool = new ArrayList<>();
    private List<View> adViewPool = new ArrayList<>();
    TikTokController.ControllerListener listener;


    /**
     * 数据源
     */
    private List<PaperTypeBean> mVideoBeans;

    public Tiktok2Adapter(List<PaperTypeBean> videoBeans, TikTokController.ControllerListener listener) {
        this.mVideoBeans = videoBeans;
        this.listener = listener;
    }

    public List<PaperTypeBean> getmVideoBeans() {
        return mVideoBeans;
    }

    @Override
    public int getCount() {
        return mVideoBeans == null ? 0 : mVideoBeans.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;
        Context context = container.getContext();
        if (mVideoBeans.get(position).isAd()) {

            if (adViewPool.size() > 0) {//取第一个进行复用
                view = adViewPool.get(0);
                adViewPool.remove(0);
            }
            if (ads.size() <= 1) {
                if (loadMoreAdsListener != null) {
                    loadMoreAdsListener.loadMore();
                }
            }
            if (ads.size() == 0) {
                if (view == null)
                    return view;
            }
            view = ads.remove(0);
            container.addView(view);
            return view;
        }
        if (mViewPool.size() > 0) {//取第一个进行复用
            view = mViewPool.get(0);
            mViewPool.remove(0);
        }
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_tik_tok_2, container, false);
            viewHolder = new ViewHolder(view, listener, mVideoBeans.get(position).isAd());
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        PaperTypeBean item = mVideoBeans.get(position);
        //开始预加载
        PreloadManager.getInstance(context).addPreloadTask(item.getVideoUrl(), position);
        Glide.with(context)
                .load(item.getPaperUrl())
                .into(viewHolder.mThumb);
        viewHolder.iv_collect.setImageResource(item.isFlg() ? R.drawable.icon_collect_video : R.drawable.icon_collect_video_uncheck);
        viewHolder.iv_collect.setOnClickListener(v -> {
            if (videoControllerListener != null) {
                videoControllerListener.onCollect(item.getVideoId(), viewHolder.iv_collect, item.isFlg());
            }
        });
        viewHolder.tv_share.setOnClickListener(v -> {
            final int TAG_SHARE_WECHAT_FRIEND = 0;
            final int TAG_SHARE_WECHAT_MOMENT = 1;
            QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(context);
            builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                    .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                    .setOnSheetItemClickListener((dialog, itemView) -> {
                        if (videoControllerListener != null)
                            videoControllerListener.onShare(item.getVideoId());
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
//                                ToastUtils.showToast("分享到微信");
                                WechatShare(context, AppConfig.ShareUrl+"?videoId="+mVideoBeans.get(position).getVideoId(), "超高清壁纸", "我推荐一张壁纸给你", true);
                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
//                                ToastUtils.showToast("分享到朋友圈");
                                WechatShare(context, AppConfig.ShareUrl+"?videoId="+mVideoBeans.get(position).getVideoId(), "超高清壁纸", "我推荐一张壁纸给你", false);
                                break;
                        }
                    }).build().show();
        });
        viewHolder.mTitle.setText(item.getVideoName());
        viewHolder.mPosition = position;
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View itemView = (View) object;
        container.removeView(itemView);
        if (mVideoBeans.get(position).isAd()) {
            adViewPool.add(itemView);
        } else {
            PaperTypeBean item = mVideoBeans.get(position);
            //取消预加载
            PreloadManager.getInstance(container.getContext()).removePreloadTask(item.getVideoUrl());
            //保存起来用来复用
            mViewPool.add(itemView);
        }
    }

    /**
     * 借鉴ListView item复用方法
     */
    public static class ViewHolder {

        public TikTokController mTikTokController;
        public VideoView mVideoView;
        public int mPosition;
        public TextView mTitle;//标题
        public ImageView mThumb;//封面图
        public ImageView iv_collect;
        private TextView tv_share;

        ViewHolder(View itemView, TikTokController.ControllerListener listener, boolean isAd) {
            if (isAd) {
                itemView.setTag(this);
                return;
            }
            mVideoView = itemView.findViewById(R.id.video_view);
            mVideoView.setLooping(true);
            mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
            mTikTokController = new TikTokController(itemView.getContext());
            mTikTokController.setListener(listener);
            mVideoView.setVideoController(mTikTokController);
            mTitle = mTikTokController.findViewById(R.id.tv_title);
            mThumb = mTikTokController.findViewById(R.id.iv_thumb);
            iv_collect = mTikTokController.findViewById(R.id.iv_collect);
            tv_share = mTikTokController.findViewById(R.id.tv_share);
            itemView.setTag(this);
        }
    }


    VideoControllerListener videoControllerListener;

    public void setVideoControllerListener(VideoControllerListener videoControllerListener) {
        this.videoControllerListener = videoControllerListener;
    }

    public void changeCheck(int videoId, boolean isCollect) {
        for (PaperTypeBean mVideoBean : mVideoBeans) {
            if (mVideoBean.getVideoId() == videoId) {
                mVideoBean.setFlg(isCollect);
            }
        }
    }

    public interface VideoControllerListener {
        void onCollect(int videoId, ImageView imageView, boolean isCollect);

        void onShare(int videoId);
    }

    loadMoreAdsListener loadMoreAdsListener;

    public void setLoadMoreAdsListener(Tiktok2Adapter.loadMoreAdsListener loadMoreAdsListener) {
        this.loadMoreAdsListener = loadMoreAdsListener;
    }

    public interface loadMoreAdsListener {
        void loadMore();
    }
}
