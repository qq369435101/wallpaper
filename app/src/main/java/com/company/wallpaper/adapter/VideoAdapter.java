package com.company.wallpaper.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.company.wallpaper.AppConfig;
import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.ui.AutoPlayRecyclerViewActivity;
import com.company.wallpaper.ui.ImageShowActivity;
import com.company.wallpaper.ui.PaperListActivity;
import com.company.wallpaper.utils.GlideCacheUtil;
import com.company.wallpaper.view.datepicker.DateFormatUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.io.IOException;
import java.util.List;

import static com.company.wallpaper.utils.wechatutils.WechatLoginHelper.WechatShare;

/**
 * Created by yushengyang.
 * Date: 2019-12-02.
 */

public class VideoAdapter extends CommonViewAdapter<PaperTypeBean> {
    int typeId;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public VideoAdapter(Context context, List<PaperTypeBean> datas) {
        super(context, datas,  R.layout.item_video_list);
        setCanShowEmpty(true);
    }

    @Override
    public void convert(ViewHolder holder, PaperTypeBean item) {
        ((TextView) holder.getView(R.id.tv_video_title)).setText(item.getVideoName());
        ((TextView) holder.getView(R.id.tv_video_type)).setText(item.getTypeName());
        holder.getView(R.id.tv_video_type).setOnClickListener(v -> PaperListActivity.toPagerListActivity(typeId,item.getTypeName(),1,mContext));
        holder.getView(R.id.iv_share).setOnClickListener(v -> {
            final int TAG_SHARE_WECHAT_FRIEND = 0;
            final int TAG_SHARE_WECHAT_MOMENT = 1;
            QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(mContext);
            builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                    .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                    .setOnSheetItemClickListener((dialog, itemView) -> {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
//                                ToastUtils.showToast("分享到微信");
                                WechatShare(mContext, AppConfig.ShareUrl, "超高清壁纸", "我推荐一张壁纸给你", true);
                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
//                                ToastUtils.showToast("分享到朋友圈");
                                WechatShare(mContext, AppConfig.ShareUrl, "超高清壁纸", "我推荐一张壁纸给你", false);
                                break;
                        }
                    }).build().show();
        });
        GlideCacheUtil.LoadImage(mContext,holder.getView(R.id.iv_main),item.getPaperUrl());
        holder.getView(R.id.ll_cl).setOnClickListener(v -> AutoPlayRecyclerViewActivity.toRecyclerViewVideoActivity(mContext,typeId,"视频"));
        try {
            MediaPlayer meidaPlayer = new MediaPlayer();
            meidaPlayer.setDataSource(item.getVideoUrl());
            meidaPlayer.prepareAsync();
            meidaPlayer.setOnPreparedListener(mp -> ((TextView) holder.getView(R.id.tv_video_time)).setText(DateFormatUtils.long2StrMS(mp.getDuration())));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
