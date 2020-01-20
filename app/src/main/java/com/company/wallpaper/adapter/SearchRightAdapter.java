package com.company.wallpaper.adapter;

import android.content.Context;
import android.view.View;

import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.ui.PaperListActivity;
import com.company.wallpaper.utils.GlideCacheUtil;

import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-11-27.
 */

public class SearchRightAdapter extends CommonViewAdapter<PaperTypeBean> {
    public SearchRightAdapter(Context context, List<PaperTypeBean> datas) {
        super(context, datas, R.layout.item_paper_content);
        setCanShowEmpty(true);
    }

    @Override
    public void convert(ViewHolder holder, PaperTypeBean item) {
        holder.setText(R.id.tv_title, item.getTypeName());
        holder.setText(R.id.tv_content, item.getTypeRepresent());
        GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.iv), item.getTypeCover());
        holder.getView(R.id.cl_item).setOnClickListener(v -> PaperListActivity.toPagerListActivity(item.getTypeId(),item.getTypeName(),0,mContext));
    }
}
