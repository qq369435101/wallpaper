package com.company.wallpaper.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;


import com.company.wallpaper.R;
import com.company.wallpaper.databinding.LayoutAlertDialogBinding;
import com.company.wallpaper.utils.DevicesUtils;

/**
 * Created by yushengyang.
 * Date: 2018/11/12.
 */

public class MyAlertDialog extends Dialog {
    private String AlertContent;
    private Context context;
    private LayoutAlertDialogBinding inflate;

    public MyAlertDialog(@NonNull Context context, String AlertContent) {
        super(context);
        this.AlertContent = AlertContent;
        this.context = context;
        init();
    }

    public void setPositiveListener(View.OnClickListener listener) {
        inflate.tvPositive.setOnClickListener(listener);
    }



    public void init() {
        inflate = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.layout_alert_dialog, null, false);
        setContentView(inflate.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(DevicesUtils.dip2px(context,280), WindowManager.LayoutParams.WRAP_CONTENT);
        inflate.tvAlertContent.setText(AlertContent);
        inflate.tvNegative.setOnClickListener(v -> dismiss());

    }

}
