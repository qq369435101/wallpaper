package com.company.wallpaper.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.company.wallpaper.R;
import com.company.wallpaper.app.SwipeBackActivity;
import com.company.wallpaper.databinding.ActivityWebViewForOutBinding;

import com.ysy.commonlib.utils.PaxWebChromeClient;
import com.ysy.commonlib.utils.StringUtils;

public class WebViewForPayActivity extends SwipeBackActivity<ActivityWebViewForOutBinding> {
    PaxWebChromeClient webChromeClient;

    int RESULT_CODE = 0;

    @Override
    @SuppressLint("JavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_for_out);
        showContentView();
//        DevicesUtils.setWindowStatusBarColor(this, R.color.lightBlue);
        setResult(300);
        bindingView.libTitleBar.getCenterTextView().setText("快捷支付");
        init();
    }

    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startapp")) {
            return true;
        } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                && (url.contains("platformapi") && url.contains("startapp"))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        webChromeClient.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void toWebView(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewForPayActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        ((Activity) context).startActivityForResult(intent, 100);
    }

    public void init() {
//        //禁止跳出app
//        bindingView.webView.setWebViewClient(new MyWebViewClient(bindingView.webView));
//        AndroidBug5497Workaround.assistActivity(this);
//        WebSettings webSettings = bindingView.webView.getSettings();
//        webSettings.setDomStorageEnabled(true);
////        bindingView.webView.setDefaultHandler(new DefaultHandler());
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
////        bindingView.webView.setDefaultHandler(new DefaultHandler());
//        webChromeClient = new PaxWebChromeClient(this);
//        bindingView.webView.setWebChromeClient(webChromeClient);
        WebSettings settings = bindingView.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        bindingView.webView.setWebViewClient(new WebViewClient() {

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.equals("https://m.one1tree.com.cn/download.html")) {
                    bindingView.libTitleBar.post(WebViewForPayActivity.this::finish);
                    return null;
                }
                return super.shouldInterceptRequest(view, url);
            }
        });
        if (!StringUtils.isEmptyString(getIntent().getStringExtra("url"))) {
            bindingView.webView.loadData(getIntent().getStringExtra("url"), "text/html;charset=utf-8", "utf-8");
        } else {
            Toast.makeText(this, "没有更多数据", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bindingView.webView != null) {
            bindingView.webView.setVisibility(View.GONE);
            bindingView.webView.removeAllViews();
            bindingView.webView.destroy();
        }
    }


}
