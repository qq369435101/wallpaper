package com.ysy.commonlib.base;


import android.app.Activity;
import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by yushengyang.
 * Date: 2019/5/21.
 */

public class BaseHandler<T extends Activity> extends Handler {
    private WeakReference<T> activity;

    public BaseHandler(T activity) {
        this.activity = new WeakReference<>(activity);
    }
}
