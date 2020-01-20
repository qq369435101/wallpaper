package com.company.wallpaper.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.company.wallpaper.R;
import com.company.wallpaper.utils.DevicesUtils;

import static com.ysy.commonlib.utils.DevicesUtils.FlymeSetStatusBarLightMode;
import static com.ysy.commonlib.utils.DevicesUtils.MIUISetStatusBarLightMode;


/**
 * Created by yushengyang.
 * Date: 2018/11/12.
 */

public class CustomDialog<T extends ViewDataBinding> extends Dialog {
    private boolean bottom = false;
    private Context context;
    private int layoutId;
    private View mPositiveButton;
    private T bindview;
    private int width = -1;

    public CustomDialog(@NonNull Context context, @LayoutRes int layoutId) {
        super(context);
        this.context = context;
        this.layoutId = layoutId;
        init();
    }

    public CustomDialog(@NonNull Context context, @LayoutRes int layoutId, int width) {
        super(context);
        this.context = context;
        this.layoutId = layoutId;
        this.width = width;
        init();
    }

    public CustomDialog(@NonNull Context context, @LayoutRes int layoutId, boolean bottom) {
        super(context, R.style.BottomDialog);
        this.context = context;
        this.layoutId = layoutId;
        this.bottom = bottom;
        init();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setPositiveListener(View.OnClickListener listener) {
        if (mPositiveButton != null)
            mPositiveButton.setOnClickListener(listener);
    }

    public T getBindView() {
        return bindview;
    }

    private void init() {
        bindview = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), layoutId, null, false);
        setContentView(bindview.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        if (width != -1) {
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setLayout(DevicesUtils.dip2px(context, width), WindowManager.LayoutParams.WRAP_CONTENT);
        } else if (bottom) {
            setCanceledOnTouchOutside(true);
            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        } else {
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        View mNegativeButton = bindview.getRoot().findViewById(R.id.tv_negative);
        mPositiveButton = bindview.getRoot().findViewById(R.id.tv_positive);
        if (mNegativeButton != null)
            mNegativeButton.setOnClickListener(v -> dismiss());

        if (bottom) {
            transparencyBar(getWindow());
        }
    }

    private void transparencyBar(Window window) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (MIUISetStatusBarLightMode(window, true)) {
                window.setFlags(67108864, 67108864);
            } else if (FlymeSetStatusBarLightMode(window, true)) {
                window.setFlags(67108864, 67108864);
            } else if (Build.VERSION.SDK_INT >= 23) {
                window.clearFlags(201326592);
                window.getDecorView().setSystemUiVisibility(1280);
                window.addFlags(-2147483648);
                window.setStatusBarColor(0);
            }
        }
    }

}
