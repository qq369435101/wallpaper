package com.company.wallpaper.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.wallpaper.R;
import com.company.wallpaper.bean.UserInfoItemBean;
import com.company.wallpaper.ui.SearchListActivity;
import com.company.wallpaper.utils.ShareUtils;
import com.company.wallpaper.utils.ToastUtils;

import java.util.List;


/**
 * Created by yushengyang.
 * Date: 2019-06-27.
 */

public class UserInfoItemAdapter extends CommonViewAdapter<UserInfoItemBean> {

    public UserInfoItemAdapter(Context context, List<UserInfoItemBean> datas) {
        super(context, datas, R.layout.item_userinfo);
    }


    @Override
    public void convert(ViewHolder holder, UserInfoItemBean item) {
        ((TextView) holder.getView(R.id.tv_name)).setText(item.getItemName());
        ((ImageView) holder.getView(R.id.iv_icon)).setImageResource(item.getResId());
        holder.getView(R.id.cl_item).setOnClickListener(v -> {
            if (ShareUtils.getUserInfo() == null) {
                ToastUtils.showToast("请先登录！");
                return;
            }
            switch (item.getItemName()) {
                case "收藏": {
                    SearchListActivity.ToSearchListActivity("我的收藏", mContext);
                    break;
                }
                case "保存": {
                    SearchListActivity.ToSearchListActivity("我的下载", mContext);
                    break;
                }
                case "分享": {
                    SearchListActivity.ToSearchListActivity("我的分享", mContext);
                    break;
                }
                case "浏览": {
                    SearchListActivity.ToSearchListActivity("我的足迹", mContext);
                    break;
                }
            }
        });

    }


}
