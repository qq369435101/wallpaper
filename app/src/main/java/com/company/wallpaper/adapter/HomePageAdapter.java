package com.company.wallpaper.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.FrameLayout;

import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.ui.ImageShowActivity;
import com.company.wallpaper.utils.GlideCacheUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-11-14.
 */

public class HomePageAdapter extends CommonViewAdapter<PaperTypeBean> {
    private List<View> ads = new ArrayList<>();

    public void addAds(View view) {
        ads.add(view);
    }

    public List<View> getAds() {
        return ads;
    }

    @IdRes
    int[] ids = {R.id.iv_main, R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4, R.id.iv_5, R.id.iv_6, R.id.iv_7, R.id.iv_8};

    public HomePageAdapter(Context context, List<PaperTypeBean> datas) {
        super(context, datas, R.layout.item_home_page);
    }

    @Override
    public void convert(ViewHolder holder, PaperTypeBean item) {
        q:
        if ((mDatas.indexOf(item) - 3) % 5 == 0) {
            if (ads.size() == 0) {
                if (loadAdListener != null) {
                    loadAdListener.needLoad();
                }
                break q;
            }
            if (holder.getView(R.id.fad) != null && ((FrameLayout) holder.getView(R.id.fad)).getChildCount() == 0) {
                holder.getView(R.id.fad).setVisibility(View.VISIBLE);
                ((FrameLayout) holder.getView(R.id.fad)).addView(ads.remove(0));
            }
        } else {
            holder.getView(R.id.fad).setVisibility(View.GONE);
        }
        ArrayList<String> imgages = new ArrayList<>();
        for (int i = 0; i < item.getPaperList().size(); i++) {
            final int j = i;
            imgages.add(item.getPaperList().get(i).getPaperUrl());
            if (holder.getView(ids[i]) != null) {
                GlideCacheUtil.LoadImage(mContext, holder.getView(ids[i]), item.getPaperList().get(i).getPaperUrl());
                holder.getView(ids[i]).setOnClickListener(v -> ImageShowActivity.toImageViewActivity(mContext, item.getPaperList().get(j).getPaperId(), item.getTypeId(), item.getPaperList().get(j).getVideoId()));
            }
        }
        holder.setText(R.id.tv_type_name, item.getTypeName());
        holder.setText(R.id.tv_title, item.getTypeRepresent());
        GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.cir_user_head), item.getTypeCover());
//        holder.getView(R.id.ll_cl).setOnClickListener(v -> ImageShowActivity.toImageViewActivity(mContext, imgages, item.getPaperList().get(0).getPaperUrl()));
    }

    @Override
    public int getLayoutId(int type) {
        if (type >= 9) {
            return R.layout.item_home_page;
        } else if (type >= 6) {
            return R.layout.item_home_page_6;
        } else if (type >= 3) {
            return R.layout.item_home_page_3;
        } else if (type >= 1) {
            return R.layout.item_home_page_2;
        } else {
            return super.getLayoutId(type);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getItemViewType(position) == NO_EMPTY) {
            if (mDatas.get(position).getPaperList() == null) {
                return EMPTY;
            }
            return mDatas.get(position).getPaperList().size();
        } else {
            return EMPTY;
        }
    }

    loadAdListener loadAdListener;

    public void setLoadAdListener(HomePageAdapter.loadAdListener loadAdListener) {
        this.loadAdListener = loadAdListener;
    }

    public interface loadAdListener {
        void needLoad();
    }
}
