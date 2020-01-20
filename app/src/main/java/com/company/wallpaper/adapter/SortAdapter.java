package com.company.wallpaper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.utils.GlideCacheUtil;

import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2018/11/22.
 */

public class SortAdapter extends CommonViewAdapter<PaperTypeBean> {
    RvListener itemClickListener;

    public SortAdapter(Context context, List<PaperTypeBean> datas, RvListener listener) {
        super(context, datas, R.layout.item_rg_classify);
        itemClickListener = listener;
//        setHasStableIds(true);
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    private int checkedPosition = 0;


    @Override
    public void convert(ViewHolder holder, PaperTypeBean item) {
        if (mDatas.indexOf(item) != checkedPosition) {
            holder.getView(R.id.ll_cl).setBackgroundColor(mContext.getResources().getColor(R.color.backgroundColor));
            holder.getView(R.id.tv_classify_name).setBackgroundColor(Color.parseColor("#AADDDDDD"));
        } else {
            holder.getView(R.id.ll_cl).setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.getView(R.id.tv_classify_name).setBackgroundColor(Color.parseColor("#DDDF3232"));
        }
        ((TextView) holder.getView(R.id.tv_classify_name)).setText(item.getTypeName());
//        ((TextView) holder.getView(R.id.tv_classify_name)).setText(item);
        GlideCacheUtil.LoadImage(mContext, holder.getView(R.id.iv_classify_img), item.getTypeCover());
        holder.getView(R.id.ll_cl).setOnClickListener(v -> itemClickListener.onItemClick(mDatas.indexOf(item)));
    }

    //RecyclerView的item点击事件
    public interface RvListener {
        void onItemClick(int typeId);
    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//
//    }
}
