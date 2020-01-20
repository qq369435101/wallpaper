package com.company.wallpaper.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.ui.ImageShowActivity;
import com.company.wallpaper.ui.VideoActivity;
import com.company.wallpaper.utils.GlideCacheUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-11-29.
 */

public class VideoListAdapter extends CommonViewAdapter<PaperTypeBean> {
    private final int Ad_Id = -1;
    private int typeId;
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

    public VideoListAdapter(Context context, List<PaperTypeBean> datas,int typeId) {
        super(context, datas, R.layout.item_grid);
        this.typeId=typeId;
    }

    @Override
    public void convert(ViewHolder holder, PaperTypeBean item) {
        if (item.isAd()) {
            if (ads.size() == 0) {
                return;
            }
            if (holder.getView(R.id.frame_ad) != null && ((FrameLayout) holder.getView(R.id.frame_ad)).getChildCount() == 0) {
                holder.getView(R.id.frame_ad).setVisibility(View.VISIBLE);
                ((FrameLayout) holder.getView(R.id.frame_ad)).addView(ads.remove(0));
            }
            return;
        }
        GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.iv_main), item.getPaperUrl());
        holder.getView(R.id.iv_live).setVisibility(View.VISIBLE);
        holder.getView(R.id.cl_item).setOnClickListener(v -> VideoActivity.ToVideoActivity(mContext, typeId,item.getVideoId(),mDatas.indexOf(item),item.getVideoUrl()));
    }

    @Override
    public int getLayoutId(int type) {
        if (type == Ad_Id) {
            return R.layout.item_ad;
        } else {
            return super.getLayoutId(type);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getItemViewType(position) == NO_EMPTY) {
            if (mDatas.get(position).isAd()) {
                return Ad_Id;
            }
            return NO_EMPTY;
        } else {
            return EMPTY;
        }
    }
}
