package com.ysy.commonlib.utils;

import android.content.Context;

import java.lang.reflect.Method;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by yushengyang.
 * Date: 2019/3/26.
 */

public class StethoUtils {
    public static void init(Context context) {
        try {
            Class<?> stethoClass = Class.forName("com.facebook.stetho.Stetho");
            Method initializeWithDefaults = stethoClass.getMethod("initializeWithDefaults", Context.class);
            initializeWithDefaults.invoke(null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OkHttpClient configureInterceptor(OkHttpClient httpClient) {
        try {
            Class<?> aClass = Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor");
            return httpClient.newBuilder().addNetworkInterceptor((Interceptor) aClass.newInstance()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpClient;
    }
}
