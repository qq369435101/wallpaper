package com.company.wallpaper.network;

import com.company.wallpaper.AppConfig;
import com.ysy.commonlib.network.retrofiturlmanager.RetrofitUrlManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by ysy on 2018/1/2.
 */

public final class HttpClient {

    private final OkHttpClient mOkHttpClient;
    private final OkHttpClient mOkHttpClientNoHeader;
    private HttpClient() {
        mOkHttpClient = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new PublicParamsInterceptor())
                .addNetworkInterceptor(HttpLogFactory.BodyInterceptor(AppConfig.ONLINE_SERVER == AppConfig.DEBUG))
                .addNetworkInterceptor(HttpLogFactory.stethoInterceptor())
                .retryOnConnectionFailure(true))
                .build();
        mOkHttpClientNoHeader = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)

                .addNetworkInterceptor(HttpLogFactory.BodyInterceptor(AppConfig.ONLINE_SERVER == AppConfig.DEBUG))
                .addNetworkInterceptor(HttpLogFactory.stethoInterceptor())
                .retryOnConnectionFailure(true))
                .build();
    }

    private static final class HttpClientHolder {
        private static final HttpClient HTTP_CLIENT = new HttpClient();
    }

    public static HttpClient getInstance() {
        return HttpClientHolder.HTTP_CLIENT;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public OkHttpClient getmOkHttpClientNoHeader() {
        return mOkHttpClientNoHeader;
    }

}
