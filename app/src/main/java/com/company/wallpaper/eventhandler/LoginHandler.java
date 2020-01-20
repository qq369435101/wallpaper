package com.company.wallpaper.eventhandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.company.wallpaper.databinding.ActivityLoginBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.PatternUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.ysy.commonlib.base.BaseEventHandler;
import com.ysy.commonlib.base.TResponse;
import com.ysy.commonlib.utils.DialogUtils;

/**
 * Created by yushengyang.
 * Date: 2018/10/16.
 */

public class LoginHandler extends BaseEventHandler {


//    private LoginViewModel viewModel;
    private Context mContext;
    private ActivityLoginBinding binding;
    private QMUITipDialog tipDialog;

    public LoginHandler(Activity activity, ActivityLoginBinding binding) {
//        this.viewModel = viewModel;
        mContext = activity;
        this.binding = binding;
    }

    public void getCode(View view) {

    }



}
