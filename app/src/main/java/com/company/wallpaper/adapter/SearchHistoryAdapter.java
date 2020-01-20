package com.company.wallpaper.adapter;

import android.content.Context;
import android.view.View;

import com.company.wallpaper.R;
import com.company.wallpaper.ui.SearchListActivity;

import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-11-19.
 */

public class SearchHistoryAdapter extends CommonViewAdapter<String> {
    public SearchHistoryAdapter(Context context, List<String> datas) {
        super(context, datas, R.layout.item_search);
    }

    @Override
    public void convert(ViewHolder holder, String item) {
        holder.setText(R.id.tv_search, item);
        holder.getView(R.id.tv_search).setOnClickListener(v -> SearchListActivity.ToSearchListActivity(item,mContext,true));

    }
}
