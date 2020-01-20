package com.company.wallpaper.utils;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.company.wallpaper.R;


/**
 * Created by yushengyang.
 * Date: 2018/12/7.
 */

public class StateCheckUtils {

    public static void checkClickable(boolean canLight, View view, Drawable drawable, View.OnClickListener listener) {
        checkClickable(canLight, view, drawable, listener, "");
    }

    public static void checkClickable(boolean canLight, View view, Drawable drawable, View.OnClickListener listener, String errorMsg) {
        if (canLight) {
            view.setClickable(true);
            view.setBackground(drawable);
            view.setOnClickListener(listener);
        } else {
            view.setClickable(false);
            if (!errorMsg.equals("")) {
                view.setOnClickListener(v -> ToastUtils.showToast(errorMsg));
            } else {
                view.setOnClickListener(null);
            }
            view.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorLineDeep));
        }
    }

    /**
     * @param canLight
     * @param view
     * @param d1       能点击时drawable
     * @param d2
     * @param listener
     */
    public static void checkClickable(boolean canLight, View view, Drawable d1, Drawable d2, View.OnClickListener listener) {
        if (canLight) {
            view.setClickable(true);
            view.setBackground(d1);
            view.setOnClickListener(listener);
        } else {
            view.setClickable(false);
            view.setOnClickListener(null);
            view.setBackground(d2);
        }
    }
}
