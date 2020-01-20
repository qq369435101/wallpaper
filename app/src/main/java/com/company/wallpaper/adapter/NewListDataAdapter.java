package com.company.wallpaper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.company.wallpaper.R;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.ui.PaperListActivity;

import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-12-01.
 */

public class NewListDataAdapter extends CommonViewAdapter<PaperTypeBean> {
    public NewListDataAdapter(Context context, List<PaperTypeBean> datas) {
        super(context, datas, R.layout.item_new_list);
    }

    @Override
    public void convert(ViewHolder holder, PaperTypeBean item) {
        holder.setText(R.id.tv_news, item.getTypeRepresent());
        holder.getView(R.id.tv_news).setVisibility(TextUtils.isEmpty(item.getTypeRepresent()) ? View.GONE : View.VISIBLE);
        holder.setText(R.id.tv_date, item.getCreatTime());
        holder.getView(R.id.cl_item).setOnClickListener(v -> PaperListActivity.toPagerListActivity(item.getTypeId(), item.getTypeName(), 1, mContext));

    }
}
