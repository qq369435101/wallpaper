package com.company.wallpaper.utils;

import android.content.Context;

/**
 * Created by smartTop on 2016/10/19.
 */

public class Dev {
    public Dev() {
    }

    public static int dp2px(Context context, float dp) {
        return (int) Math.ceil((double)(context.getResources().getDisplayMetrics().density * dp));
    }
    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }
}

