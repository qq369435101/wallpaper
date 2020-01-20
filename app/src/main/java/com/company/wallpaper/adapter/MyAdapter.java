package com.company.wallpaper.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bytedance.sdk.openadsdk.DownloadStatusController;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.ui.LoadMoreView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by yushengyang.
 * Date: 2019-12-22.
 */

public class MyAdapter
//        extends RecyclerView.Adapter
{
//    private static final int FOOTER_VIEW_COUNT = 1;
//
//    private static final int ITEM_VIEW_TYPE_LOAD_MORE = -1;
//    private static final int ITEM_VIEW_TYPE_NORMAL = 0;
//    private static final int ITEM_VIEW_TYPE_GROUP_PIC_AD = 1;
//    private static final int ITEM_VIEW_TYPE_SMALL_PIC_AD = 2;
//    private static final int ITEM_VIEW_TYPE_LARGE_PIC_AD = 3;
//    private static final int ITEM_VIEW_TYPE_VIDEO = 4;
//    private static final int ITEM_VIEW_TYPE_VERTICAL_PIC_AD = 5;//竖版图片
//
//    private List<PaperTypeBean> mData;
//    private Context mContext;
//    private RequestManager mRequestManager;
//    private Map<AdViewHolder, TTAppDownloadListener> mTTAppDownloadListenerMap = new WeakHashMap<>();
//
//    public PaperTypeBean getData(int position) {
//        if (mData.size() < position) {
//            return null;
//        }
//        return mData.get(position);
//    }
//
//    public MyAdapter(Context context, List<PaperTypeBean> data) {
//        this.mContext = context;
//        this.mData = data;
//        mRequestManager = Glide.with(mContext);
//    }
//
//
//    public void add(List<PaperTypeBean> mData) {
//        this.mData.addAll(mData);
//        notifyDataSetChanged();
//    }
//
//    public void add(PaperTypeBean mData) {
//        this.mData.add(mData);
//        notifyDataSetChanged();
//    }
//
//    public void add(PaperTypeBean mData, int position) {
//        this.mData.add(position, mData);
//        notifyDataSetChanged();
//    }
//    public void set(PaperTypeBean mData, int position) {
//        this.mData.set(position, mData);
//        notifyDataSetChanged();
//    }
//    public List<PaperTypeBean> getmData() {
//        return mData;
//    }
//
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        RecyclerView.ViewHolder holder = null;
//        switch (viewType) {
//            case ITEM_VIEW_TYPE_LOAD_MORE:
//                return new LoadMoreViewHolder(new LoadMoreView(mContext));
//            case ITEM_VIEW_TYPE_SMALL_PIC_AD:
//                return new SmallAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_small_pic, parent, false));
//            case ITEM_VIEW_TYPE_LARGE_PIC_AD:
//                return new LargeAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_large_pic, parent, false));
//            case ITEM_VIEW_TYPE_VERTICAL_PIC_AD:
//                return new VerticalAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_vertical_pic, parent, false));
//            case ITEM_VIEW_TYPE_GROUP_PIC_AD:
//                return new GroupAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_group_pic, parent, false));
//            case ITEM_VIEW_TYPE_VIDEO:
//                return new VideoAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_large_video, parent, false));
//            default:
//                return new NormalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_normal, parent, false));
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        int count = mData.size();
//        TTNativeExpressAd TTNativeExpressAd;
//        if (holder instanceof SmallAdViewHolder) {
//            TTNativeExpressAd = mData.get(position).getAd();
//            SmallAdViewHolder smallAdViewHolder = (SmallAdViewHolder) holder;
//            bindData(smallAdViewHolder, TTNativeExpressAd);
//            if (TTNativeExpressAd.getImageList() != null && !TTNativeExpressAd.getImageList().isEmpty()) {
//                TTImage image = TTNativeExpressAd.getImageList().get(0);
//                if (image != null && image.isValid()) {
//                    mRequestManager.load(image.getImageUrl()).into(smallAdViewHolder.mSmallImage);
//                }
//            }
//
//        } else if (holder instanceof LargeAdViewHolder) {
//            TTNativeExpressAd = mData.get(position).getAd();
//            LargeAdViewHolder largeAdViewHolder = (LargeAdViewHolder) holder;
//            bindData(largeAdViewHolder, TTNativeExpressAd);
//            if (TTNativeExpressAd.getImageList() != null && !TTNativeExpressAd.getImageList().isEmpty()) {
//                TTImage image = TTNativeExpressAd.getImageList().get(0);
//                if (image != null && image.isValid()) {
//                    mRequestManager.load(image.getImageUrl()).into(largeAdViewHolder.mLargeImage);
//                }
//            }
//
//        } else if (holder instanceof GroupAdViewHolder) {
//            TTNativeExpressAd = mData.get(position).getAd();
//            GroupAdViewHolder groupAdViewHolder = (GroupAdViewHolder) holder;
//            bindData(groupAdViewHolder, TTNativeExpressAd);
//            if (TTNativeExpressAd.getImageList() != null && TTNativeExpressAd.getImageList().size() >= 3) {
//                TTImage image1 = TTNativeExpressAd.getImageList().get(0);
//                TTImage image2 = TTNativeExpressAd.getImageList().get(1);
//                TTImage image3 = TTNativeExpressAd.getImageList().get(2);
//                if (image1 != null && image1.isValid()) {
//                    mRequestManager.load(image1.getImageUrl()).into(groupAdViewHolder.mGroupImage1);
//                }
//                if (image2 != null && image2.isValid()) {
//                    mRequestManager.load(image2.getImageUrl()).into(groupAdViewHolder.mGroupImage2);
//                }
//                if (image3 != null && image3.isValid()) {
//                    mRequestManager.load(image3.getImageUrl()).into(groupAdViewHolder.mGroupImage3);
//                }
//            }
//
//        } else if (holder instanceof VideoAdViewHolder) {
//            TTNativeExpressAd = mData.get(position).getAd();
//            VideoAdViewHolder videoAdViewHolder = (VideoAdViewHolder) holder;
//            bindData(videoAdViewHolder, TTNativeExpressAd);
//            TTNativeExpressAd.setVideoAdListener(new TTNativeExpressAd.VideoAdListener() {
//                @Override
//                public void onVideoLoad(TTNativeExpressAd ad) {
//
//                }
//
//                @Override
//                public void onVideoError(int errorCode, int extraCode) {
//
//                }
//
//                @Override
//                public void onVideoAdStartPlay(TTNativeExpressAd ad) {
//
//                }
//
//                @Override
//                public void onVideoAdPaused(TTNativeExpressAd ad) {
//
//                }
//
//                @Override
//                public void onVideoAdContinuePlay(TTNativeExpressAd ad) {
//
//                }
//
//                @Override
//                public void onProgressUpdate(long current, long duration) {
//
//                }
//
//                @Override
//                public void onVideoAdComplete(TTNativeExpressAd ad) {
//
//                }
//            });
//            if (videoAdViewHolder.videoView != null) {
//                View video = TTNativeExpressAd.getAdView();
//                if (video != null) {
//                    if (video.getParent() == null) {
//                        videoAdViewHolder.videoView.removeAllViews();
//                        videoAdViewHolder.videoView.addView(video);
//                    }
//                }
//            }
//
//        } else if (holder instanceof NormalViewHolder) {
//            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
//            normalViewHolder.idle.setText("Recycler item " + position);
//        } else if (holder instanceof LoadMoreViewHolder) {
//            LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;
//        }
//
//        if (holder instanceof LoadMoreViewHolder) {
//            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
//        } else if (holder instanceof SmallAdViewHolder ||
//                holder instanceof VideoAdViewHolder ||
//                holder instanceof LargeAdViewHolder ||
//                holder instanceof GroupAdViewHolder) {
//            holder.itemView.setBackgroundColor(Color.WHITE);
//        } else {
//            holder.itemView.setBackgroundColor(getColorRandom());
//        }
//    }
//
//    private int getColorRandom() {
//        int a = Double.valueOf(Math.random() * 255).intValue();
//        int r = Double.valueOf(Math.random() * 255).intValue();
//        int g = Double.valueOf(Math.random() * 255).intValue();
//        int b = Double.valueOf(Math.random() * 255).intValue();
//        return Color.argb(a, r, g, b);
//    }
//
//    private void bindData(final AdViewHolder adViewHolder, TTNativeExpressAd ad) {
//        //可以被点击的view, 也可以把convertView放进来意味item可被点击
//        List<View> clickViewList = new ArrayList<>();
//        clickViewList.add(adViewHolder.itemView);
//        //触发创意广告的view（点击下载或拨打电话）
//        List<View> creativeViewList = new ArrayList<>();
//        creativeViewList.add(adViewHolder.mCreativeButton);
//        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入
////            creativeViewList.add(convertView);
//        //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
//        ad.registerViewForInteraction((ViewGroup) adViewHolder.itemView, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
//            @Override
//            public void onAdClicked(View view, TTNativeAd ad) {
//                if (ad != null) {
////                    TToast.show(mContext, "广告" + ad.getTitle() + "被点击");
//                }
//            }
//
//            @Override
//            public void onAdCreativeClick(View view, TTNativeAd ad) {
//                if (ad != null) {
////                    TToast.show(mContext, "广告" + ad.getTitle() + "被创意按钮被点击");
//                }
//            }
//
//            @Override
//            public void onAdShow(TTNativeAd ad) {
//                if (ad != null) {
////                    TToast.show(mContext, "广告" + ad.getTitle() + "展示");
//                }
//            }
//        });
//        adViewHolder.mTitle.setText(ad.getTitle());
//        adViewHolder.mDescription.setText(ad.getDescription());
//        adViewHolder.mSource.setText(ad.getSource() == null ? "广告来源" : ad.getSource());
//        TTImage icon = ad.getIcon();
//        if (icon != null && icon.isValid()) {
//            mRequestManager.load(icon.getImageUrl()).into(adViewHolder.mIcon);
//        }
//        Button adCreativeButton = adViewHolder.mCreativeButton;
//        switch (ad.getInteractionType()) {
//            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
//                //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
//                if (mContext instanceof Activity) {
//                    ad.setActivityForDownloadApp((Activity) mContext);
//                }
//                adCreativeButton.setVisibility(View.VISIBLE);
//                adViewHolder.mStopButton.setVisibility(View.VISIBLE);
//                adViewHolder.mRemoveButton.setVisibility(View.VISIBLE);
//                bindDownloadListener(adCreativeButton, adViewHolder, ad);
//                //绑定下载状态控制器
//                bindDownLoadStatusController(adViewHolder, ad);
//                break;
//            case TTAdConstant.INTERACTION_TYPE_DIAL:
//                adCreativeButton.setVisibility(View.VISIBLE);
//                adCreativeButton.setText("立即拨打");
//                adViewHolder.mStopButton.setVisibility(View.GONE);
//                adViewHolder.mRemoveButton.setVisibility(View.GONE);
//                break;
//            case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
//            case TTAdConstant.INTERACTION_TYPE_BROWSER:
////                    adCreativeButton.setVisibility(View.GONE);
//                adCreativeButton.setVisibility(View.VISIBLE);
//                adCreativeButton.setText("查看详情");
//                adViewHolder.mStopButton.setVisibility(View.GONE);
//                adViewHolder.mRemoveButton.setVisibility(View.GONE);
//                break;
//            default:
//                adCreativeButton.setVisibility(View.GONE);
//                adViewHolder.mStopButton.setVisibility(View.GONE);
//                adViewHolder.mRemoveButton.setVisibility(View.GONE);
////                TToast.show(mContext, "交互类型异常");
//        }
//    }
//
//    private void bindDownLoadStatusController(AdViewHolder adViewHolder, final TTNativeExpressAd ad) {
//        final DownloadStatusController controller = ad.getDownloadStatusController();
//        adViewHolder.mStopButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (controller != null) {
//                    controller.changeDownloadStatus();
////                    TToast.show(mContext, "改变下载状态");
////                    Log.d(TAG, "改变下载状态");
//                }
//            }
//        });
//
//        adViewHolder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (controller != null) {
//                    controller.cancelDownload();
////                    TToast.show(mContext, "取消下载");
////                    Log.d(TAG, "取消下载");
//                }
//            }
//        });
//    }
//
//    private void bindDownloadListener(final Button adCreativeButton, final AdViewHolder adViewHolder, TTNativeExpressAd ad) {
//        TTAppDownloadListener downloadListener = new TTAppDownloadListener() {
//            @Override
//            public void onIdle() {
//                if (!isValid()) {
//                    return;
//                }
//                adCreativeButton.setText("开始下载");
//                adViewHolder.mStopButton.setText("开始下载");
//            }
//
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
//                if (totalBytes <= 0L) {
//                    adCreativeButton.setText("0%");
//                } else {
//                    adCreativeButton.setText((currBytes * 100 / totalBytes) + "%");
//                }
//                adViewHolder.mStopButton.setText("下载中");
//            }
//
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
//                if (totalBytes <= 0L) {
//                    adCreativeButton.setText("0%");
//                } else {
//                    adCreativeButton.setText((currBytes * 100 / totalBytes) + "%");
//                }
//                adViewHolder.mStopButton.setText("下载暂停");
//            }
//
//            @Override
//            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
//                adCreativeButton.setText("重新下载");
//                adViewHolder.mStopButton.setText("重新下载");
//            }
//
//            @Override
//            public void onInstalled(String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
//                adCreativeButton.setText("点击打开");
//                adViewHolder.mStopButton.setText("点击打开");
//            }
//
//            @Override
//            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                if (!isValid()) {
//                    return;
//                }
//                adCreativeButton.setText("点击安装");
//                adViewHolder.mStopButton.setText("点击安装");
//            }
//
//            @SuppressWarnings("BooleanMethodIsAlwaysInverted")
//            private boolean isValid() {
//                return mTTAppDownloadListenerMap.get(adViewHolder) == this;
//            }
//        };
//        //一个ViewHolder对应一个downloadListener, isValid判断当前ViewHolder绑定的listener是不是自己
//        ad.setDownloadListener(downloadListener); // 注册下载监听器
//        mTTAppDownloadListenerMap.put(adViewHolder, downloadListener);
//    }
//
//    @Override
//    public int getItemCount() {
//        int count = mData == null ? 0 : mData.size();
//        return count + FOOTER_VIEW_COUNT;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (mData != null) {
//            int count = mData.size();
//            if (position >= count) {
//                return ITEM_VIEW_TYPE_LOAD_MORE;
//            } else {
//                TTNativeExpressAd ad = mData.get(position).getAd();
//                if (ad == null) {
//                    return ITEM_VIEW_TYPE_NORMAL;
//                } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_SMALL_IMG) {
//                    return ITEM_VIEW_TYPE_SMALL_PIC_AD;
//                } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_LARGE_IMG) {
//                    return ITEM_VIEW_TYPE_LARGE_PIC_AD;
//                } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_GROUP_IMG) {
//                    return ITEM_VIEW_TYPE_GROUP_PIC_AD;
//                } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_VIDEO) {
//                    return ITEM_VIEW_TYPE_VIDEO;
//                } else if (ad.getImageMode() == TTAdConstant.IMAGE_MODE_VERTICAL_IMG) {//竖版图片
//                    return ITEM_VIEW_TYPE_VERTICAL_PIC_AD;
//                } else {
////                    TToast.show(mContext, "图片展示样式错误");
//                    return ITEM_VIEW_TYPE_NORMAL;
//                }
//            }
//
//        }
//        return super.getItemViewType(position);
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//
//        RecyclerView.LayoutManager layout = recyclerView.getLayoutManager();
//        if (layout != null && layout instanceof GridLayoutManager) {
//            final GridLayoutManager manager = (GridLayoutManager) layout;
//            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    int type = getItemViewType(position);
//                    if (type == ITEM_VIEW_TYPE_LOAD_MORE || type == ITEM_VIEW_TYPE_VIDEO) {
//                        return manager.getSpanCount();
//                    }
//                    return 1;
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
//        //noinspection unchecked
//        super.onViewAttachedToWindow(holder);
//        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
//            int position = holder.getLayoutPosition();
//            int type = getItemViewType(position);
//            if (type == ITEM_VIEW_TYPE_LOAD_MORE || type == ITEM_VIEW_TYPE_VIDEO) {
//                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//                p.setFullSpan(true);
//            }
//        }
//    }
//
//    public int getDataSize() {
//        return mData.size();
//    }
//
//    public void clear() {
//        mData.clear();
//        notifyDataSetChanged();
//    }
//
//    @SuppressWarnings("WeakerAccess")
//    private static class VideoAdViewHolder extends AdViewHolder {
//        @SuppressWarnings("CanBeFinal")
//        FrameLayout videoView;
//
//        @SuppressWarnings("RedundantCast")
//        public VideoAdViewHolder(View itemView) {
//            super(itemView);
//
//            mTitle = (TextView) itemView.findViewById(R.id.tv_listitem_ad_title);
//            mDescription = (TextView) itemView.findViewById(R.id.tv_listitem_ad_desc);
//            mSource = (TextView) itemView.findViewById(R.id.tv_listitem_ad_source);
//            videoView = (FrameLayout) itemView.findViewById(R.id.iv_listitem_video);
//            mIcon = (ImageView) itemView.findViewById(R.id.iv_listitem_icon);
//            mCreativeButton = (Button) itemView.findViewById(R.id.btn_listitem_creative);
//            mStopButton = (Button) itemView.findViewById(R.id.btn_listitem_stop);
//            mRemoveButton = (Button) itemView.findViewById(R.id.btn_listitem_remove);
//
//        }
//    }
//
//    private static class LargeAdViewHolder extends AdViewHolder {
//        ImageView mLargeImage;
//
//        @SuppressWarnings("RedundantCast")
//        public LargeAdViewHolder(View itemView) {
//            super(itemView);
//
//            mTitle = (TextView) itemView.findViewById(R.id.tv_listitem_ad_title);
//            mDescription = (TextView) itemView.findViewById(R.id.tv_listitem_ad_desc);
//            mSource = (TextView) itemView.findViewById(R.id.tv_listitem_ad_source);
//            mLargeImage = (ImageView) itemView.findViewById(R.id.iv_listitem_image);
//            mIcon = (ImageView) itemView.findViewById(R.id.iv_listitem_icon);
//            mCreativeButton = (Button) itemView.findViewById(R.id.btn_listitem_creative);
//            mStopButton = (Button) itemView.findViewById(R.id.btn_listitem_stop);
//            mRemoveButton = (Button) itemView.findViewById(R.id.btn_listitem_remove);
//        }
//    }
//
//    private static class SmallAdViewHolder extends AdViewHolder {
//        ImageView mSmallImage;
//
//        @SuppressWarnings("RedundantCast")
//        public SmallAdViewHolder(View itemView) {
//            super(itemView);
//
//            mTitle = (TextView) itemView.findViewById(R.id.tv_listitem_ad_title);
//            mSource = (TextView) itemView.findViewById(R.id.tv_listitem_ad_source);
//            mDescription = (TextView) itemView.findViewById(R.id.tv_listitem_ad_desc);
//            mSmallImage = (ImageView) itemView.findViewById(R.id.iv_listitem_image);
//            mIcon = (ImageView) itemView.findViewById(R.id.iv_listitem_icon);
//            mCreativeButton = (Button) itemView.findViewById(R.id.btn_listitem_creative);
//            mStopButton = (Button) itemView.findViewById(R.id.btn_listitem_stop);
//            mRemoveButton = (Button) itemView.findViewById(R.id.btn_listitem_remove);
//        }
//    }
//
//    private static class VerticalAdViewHolder extends AdViewHolder {
//        ImageView mVerticalImage;
//
//        @SuppressWarnings("RedundantCast")
//        public VerticalAdViewHolder(View itemView) {
//            super(itemView);
//
//            mTitle = (TextView) itemView.findViewById(R.id.tv_listitem_ad_title);
//            mSource = (TextView) itemView.findViewById(R.id.tv_listitem_ad_source);
//            mDescription = (TextView) itemView.findViewById(R.id.tv_listitem_ad_desc);
//            mVerticalImage = (ImageView) itemView.findViewById(R.id.iv_listitem_image);
//            mIcon = (ImageView) itemView.findViewById(R.id.iv_listitem_icon);
//            mCreativeButton = (Button) itemView.findViewById(R.id.btn_listitem_creative);
//            mStopButton = (Button) itemView.findViewById(R.id.btn_listitem_stop);
//            mRemoveButton = (Button) itemView.findViewById(R.id.btn_listitem_remove);
//        }
//    }
//
//
//    @SuppressWarnings("CanBeFinal")
//    private static class GroupAdViewHolder extends AdViewHolder {
//        ImageView mGroupImage1;
//        ImageView mGroupImage2;
//        ImageView mGroupImage3;
//
//        @SuppressWarnings("RedundantCast")
//        public GroupAdViewHolder(View itemView) {
//            super(itemView);
//
//            mTitle = (TextView) itemView.findViewById(R.id.tv_listitem_ad_title);
//            mSource = (TextView) itemView.findViewById(R.id.tv_listitem_ad_source);
//            mDescription = (TextView) itemView.findViewById(R.id.tv_listitem_ad_desc);
//            mGroupImage1 = (ImageView) itemView.findViewById(R.id.iv_listitem_image1);
//            mGroupImage2 = (ImageView) itemView.findViewById(R.id.iv_listitem_image2);
//            mGroupImage3 = (ImageView) itemView.findViewById(R.id.iv_listitem_image3);
//            mIcon = (ImageView) itemView.findViewById(R.id.iv_listitem_icon);
//            mCreativeButton = (Button) itemView.findViewById(R.id.btn_listitem_creative);
//            mStopButton = (Button) itemView.findViewById(R.id.btn_listitem_stop);
//            mRemoveButton = (Button) itemView.findViewById(R.id.btn_listitem_remove);
//        }
//    }
//
//    private static class AdViewHolder extends RecyclerView.ViewHolder {
//        ImageView mIcon;
//        Button mCreativeButton;
//        TextView mTitle;
//        TextView mDescription;
//        TextView mSource;
//        Button mStopButton;
//        Button mRemoveButton;
//
//        public AdViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    private static class NormalViewHolder extends RecyclerView.ViewHolder {
//        TextView idle;
//
//        @SuppressWarnings("RedundantCast")
//        public NormalViewHolder(View itemView) {
//            super(itemView);
//
//            idle = (TextView) itemView.findViewById(R.id.text_idle);
//
//        }
//    }
//
//    @SuppressWarnings({"CanBeFinal", "WeakerAccess"})
//    private static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
//        TextView mTextView;
//        ProgressBar mProgressBar;
//
//        @SuppressWarnings("RedundantCast")
//        public LoadMoreViewHolder(View itemView) {
//            super(itemView);
//
//            itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
//
//            mTextView = (TextView) itemView.findViewById(R.id.tv_load_more_tip);
//            mProgressBar = (ProgressBar) itemView.findViewById(R.id.pb_load_more_progress);
//        }
//    }
}
