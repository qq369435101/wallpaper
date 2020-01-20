package com.company.wallpaper.bean;

import android.support.annotation.DrawableRes;

/**
 * Created by yushengyang.
 * Date: 2019-06-27.
 */

public class UserInfoItemBean {
    @DrawableRes
    private int resId;
    private String itemName;
    private String ImageUrl;

    public int getResId() {
        return resId;
    }

    public void setResId(@DrawableRes int resId) {
        this.resId = resId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
