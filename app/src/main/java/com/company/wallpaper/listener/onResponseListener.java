package com.company.wallpaper.listener;

/**
 * Created by yushengyang.
 * Date: 2018/10/9.
 */

public interface onResponseListener<T> {
    void onSuccess(T t);

    void onFailed(Throwable throwable);

}
