package com.company.wallpaper.network;

import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.company.wallpaper.app.MyApplication;
import com.company.wallpaper.utils.SystemUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ysy on 2018/1/2.
 */

public class PublicParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = null;
        try {
            request = chain.request()
                    .newBuilder()
                    .addHeader("Cookie", TextUtils.isEmpty(MyApplication.Cookie) ? "" : MyApplication.Cookie)
                    .addHeader("OnlyID", SystemUtils.getDeviceId(MyApplication.getInstance()))
                    .addHeader("PhoneType", SystemUtils.getSystemModel())
                    .addHeader("SystemVersion", SystemUtils.getSystemVersion())
                    .addHeader("AppVersion", MyApplication.getInstance().getPackageManager().getPackageInfo(MyApplication.getInstance().getPackageName(), 0).versionName).build();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return chain.proceed(request);
    }
}
