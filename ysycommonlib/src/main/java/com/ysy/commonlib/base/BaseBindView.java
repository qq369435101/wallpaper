package com.ysy.commonlib.base;

import android.app.Activity;
import android.databinding.BindingAdapter;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

/**
 * Created by yushengyang.
 * Date: 2018/11/27.
 */

public class BaseBindView {
    @BindingAdapter({"android:onBack"})
    public static void onBack(CommonTitleBar titleBar, boolean back) {
        titleBar.setListener((v, action, extra) -> {
            if (action == CommonTitleBar.ACTION_LEFT_BUTTON && back) {
                ((Activity) titleBar.getContext()).onBackPressed();
            }
        });
    }

}
