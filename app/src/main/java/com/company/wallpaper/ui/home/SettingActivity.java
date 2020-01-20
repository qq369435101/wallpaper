package com.company.wallpaper.ui.home;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.company.wallpaper.R;
import com.company.wallpaper.app.MyApplication;
import com.company.wallpaper.app.SwipeBackActivity;
import com.company.wallpaper.databinding.ActivitySettingBinding;
import com.company.wallpaper.eventhandler.AppBaseEventHandler;
import com.company.wallpaper.ui.WebViewActivity;
import com.company.wallpaper.utils.GlideCacheUtil;
import com.company.wallpaper.utils.ShareUtils;
import com.ysy.commonlib.utils.DialogUtils;

public class SettingActivity extends SwipeBackActivity<ActivitySettingBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        showContentView();
        bindingView.tvWeb.setText(GlideCacheUtil.getInstance().getCacheSize(getApplicationContext()));
        bindingView.tvWeb.setOnClickListener(v -> {
            GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
            DialogUtils.showSuccessDialog(this, v, "缓存清除成功~");
            bindingView.tvWeb.setText("0.0byte");
        });
        try {
            bindingView.tvVersion.setText(MyApplication.getInstance().getPackageManager().getPackageInfo(MyApplication.getInstance().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        bindingView.tvAboutUs.setOnClickListener(v -> WebViewActivity.toWebView(SettingActivity.this, "关于我们", "http://www.rioses.top/?act=about"));
        bindingView.tvRegister.setOnClickListener(v -> WebViewActivity.toWebView(SettingActivity.this, "注册协议", "http://www.rioses.top/?act=zyPrivacy"));
        bindingView.tvPersonal.setOnClickListener(v -> WebViewActivity.toWebView(SettingActivity.this, "隐私协议", "http://www.rioses.top/?act=dyPrivacy"));
    }

    public void LoginOut(View view) {
        new AppBaseEventHandler().defaultDialog("确定退出登录吗？", view.getContext(), v -> {
            ShareUtils.clearUserInfo();
            ShareUtils.clearToken();
            ((Activity) view.getContext()).finish();
        });
    }
}
