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
import com.company.wallpaper.databinding.LayoutCustomDialogBinding;
import com.company.wallpaper.utils.DevicesUtils;

/**
 * Created by yushengyang.
 * Date: 2018/11/12.
 */

public class CustomAlertDialog extends Dialog {
    private String AlertContent;
    private Context context;
    private LayoutCustomDialogBinding inflate;
    private String title;

    public CustomAlertDialog(@NonNull Context context, String AlertContent, String title) {
        super(context);
        this.AlertContent = AlertContent;
        this.context = context;
        this.title = title;
        init();
    }

    public void setPositiveListener(View.OnClickListener listener) {
        inflate.tvPositive.setOnClickListener(listener);
    }


    public void setHiddenNegativeButton(boolean VISIBLE) {
        if (VISIBLE) {
            inflate.tvNegative.setVisibility(View.VISIBLE);
        } else {
            inflate.tvNegative.setVisibility(View.GONE);
        }
    }

    public void init() {
        inflate = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.layout_custom_dialog, null, false);
        setContentView(inflate.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(DevicesUtils.dip2px(context, 280), WindowManager.LayoutParams.WRAP_CONTENT);
        inflate.tvContent.setText(AlertContent);
        inflate.tvTitle.setText(title);
        inflate.tvNegative.setOnClickListener(v -> dismiss());

    }

}
