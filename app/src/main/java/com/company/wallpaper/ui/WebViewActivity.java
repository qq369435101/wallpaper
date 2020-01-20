package com.company.wallpaper.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.JsonParser;
import com.company.wallpaper.app.SwipeBackActivity;
import com.ysy.commonlib.R;
import com.ysy.commonlib.databinding.ActivityWebViewBinding;
import com.ysy.commonlib.utils.AndroidBug5497Workaround;
import com.ysy.commonlib.utils.PaxWebChromeClient;
import com.ysy.commonlib.utils.StringUtils;

public class WebViewActivity extends SwipeBackActivity<ActivityWebViewBinding> {
    PaxWebChromeClient webChromeClient;

    int RESULT_CODE = 0;

    @Override
    @SuppressLint("JavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        showContentView();
        bindingView.libTitleBar.getCenterTextView().setText(getIntent().getStringExtra("title"));
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
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    public void init() {
//        //禁止跳出app
//        bindingView.webView.setWebViewClient(new MyWebViewClient(bindingView.webView));
        AndroidBug5497Workaround.assistActivity(this);
        WebSettings webSettings = bindingView.webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        bindingView.webView.setDefaultHandler(new DefaultHandler());
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        bindingView.webView.setDefaultHandler(new DefaultHandler());
        webChromeClient = new PaxWebChromeClient(this);
        bindingView.webView.setWebChromeClient(webChromeClient);
        if (!StringUtils.isEmptyString(getIntent().getStringExtra("url"))) {
            bindingView.webView.loadUrl(getIntent().getStringExtra("url"));
        } else {
            Toast.makeText(this, "没有更多数据", Toast.LENGTH_SHORT).show();
        }

        bindingView.webView.registerHandler("getMsg", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
            }
        });


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


    /**
     * Created by zhiguo on 2016/7/5.
     */
    public class MyWebViewClient extends BridgeWebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            new Thread(() -> view.loadUrl(url));
            return true;
        }

        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

}
