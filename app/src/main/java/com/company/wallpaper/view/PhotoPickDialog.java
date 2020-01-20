package com.company.wallpaper.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.company.wallpaper.R;
import com.company.wallpaper.databinding.PopupWinSelectBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ysy.commonlib.utils.DialogUtils;

import java.io.File;

import me.nereo.multi_image_selector.MultiImageSelector;

import static com.ysy.commonlib.utils.DevicesUtils.FlymeSetStatusBarLightMode;
import static com.ysy.commonlib.utils.DevicesUtils.MIUISetStatusBarLightMode;

/**
 * Created by yushengyang.
 * Date: 2019/4/29.
 */

public class PhotoPickDialog extends Dialog {
    private File tempFile;
    Context context;
    PopupWinSelectBinding databing;
    int requestCode;
    PhotoListener listener;

    public PhotoPickDialog(@NonNull Context context, int requestCode, PhotoListener listener) {
        super(context, R.style.BottomDialog);
        this.context = context;
        this.requestCode = requestCode;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databing = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.popup_win_select, null, false);
        setCancelable(true);
        setContentView(databing.getRoot());
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        transparencyBar(getWindow());
        databing.btnCancel.setOnClickListener(v -> dismiss());
        databing.btnCamera.setOnClickListener(v -> new RxPermissions(((Activity) context)).request(
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        ).subscribe(granted -> {
            if (!granted) {
                DialogUtils.showFailedDialog(context, databing.btnCamera, "无权限无法上传图片！");
            } else {
                // Multi image selector form an Activity
                listener.takePhoto(requestCode);
                dismiss();
            }
        }));
        databing.btnPhoto.setOnClickListener(v -> new RxPermissions(((Activity) context)).request(
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(granted -> {
            if (!granted) {
                DialogUtils.showFailedDialog(context, databing.btnCamera, "无权限无法上传图片！");
            } else {
                // Multi image selector form an Activity
                MultiImageSelector.create(context).single().showCamera(false)
                        .start(((Activity) context), requestCode);
                dismiss();
            }
        }));
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


    public interface PhotoListener {
        void takePhoto(int requestCode);
    }
}
