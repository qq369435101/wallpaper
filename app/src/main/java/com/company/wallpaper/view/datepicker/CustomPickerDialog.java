package com.company.wallpaper.view.datepicker;

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
import com.company.wallpaper.databinding.DialogCustomPickerBinding;

import java.util.List;

import static com.ysy.commonlib.utils.DevicesUtils.FlymeSetStatusBarLightMode;
import static com.ysy.commonlib.utils.DevicesUtils.MIUISetStatusBarLightMode;

/**
 * Created by yushengyang.
 * Date: 2018/12/29.
 */

public class CustomPickerDialog extends Dialog {
    private List<String> mdatas;
    Context context;
    DialogCustomPickerBinding databing;
    String title;
    private onPickListener listener;

    public CustomPickerDialog(@NonNull Context context, List<String> mdatas, String title, onPickListener listener) {
        super(context, R.style.BottomDialog);
        this.context = context;
        this.mdatas = mdatas;
        this.title = title;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databing = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.dialog_custom_picker, null, false);
        setCancelable(true);
        setContentView(databing.getRoot());
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        transparencyBar(getWindow());
        // 不允许循环滚动
        databing.dpvData.setCanScrollLoop(false);
        databing.dpvData.setDataList(mdatas);
        databing.tvTitle.setText(title);
        databing.dpvData.setCanShowAnim(false);
        databing.tvCancel.setOnClickListener(v -> dismiss());
        databing.tvConfirm.setOnClickListener(v -> {
            listener.onPick(mdatas.get(databing.dpvData.getSelectedIndex()));
            dismiss();
        });
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

    public interface onPickListener {
        void onPick(String data);
    }
}
