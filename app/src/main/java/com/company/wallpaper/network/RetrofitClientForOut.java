package com.company.wallpaper.network;


import com.company.wallpaper.AppConfig;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ysy on 2018/9/2.
 */

public final class RetrofitClientForOut {

    private static final String BASE_URL;

    public static final String MiNi_Program_URL;

    static {
        switch (AppConfig.ONLINE_SERVER) {
            case AppConfig.OTHER: {
                BASE_URL = AppConfig.serviceAddress;
                MiNi_Program_URL = AppConfig.miniProAddress;
                break;
            }
            case AppConfig.DEBUG:
                BASE_URL = AppConfig.serviceTestAddress;
                MiNi_Program_URL = AppConfig.miniProTestAddress;
                break;
            case AppConfig.RELEASE:
                BASE_URL = AppConfig.serviceAddress;
                MiNi_Program_URL = AppConfig.miniProAddress;
                break;
            default:
                BASE_URL = AppConfig.serviceAddress;
                MiNi_Program_URL = AppConfig.miniProAddress;
                break;
        }
    }

    private final Retrofit mRetrofit;

    private Map<Class, Object> mServiceMap = new HashMap<>();


    private RetrofitClientForOut() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(HttpClient.getInstance().getmOkHttpClientNoHeader())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build();
    }

    private static final class RetrofitClientHolder {
        private static final RetrofitClientForOut RETROFIT_CLIENT = new RetrofitClientForOut();
    }

    public static RetrofitClientForOut getInstance() {
        return RetrofitClientHolder.RETROFIT_CLIENT;
    }

    public final <T> T getService(Class<T> tClass) {
        Object service = mServiceMap.get(tClass);
        if (service == null) {
            service = mRetrofit.create(tClass);
            mServiceMap.put(tClass, service);
        }
        return ((T) service);
    }

    public static <T> T createApi(Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")
                .client(HttpClient.getInstance().getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }
}
