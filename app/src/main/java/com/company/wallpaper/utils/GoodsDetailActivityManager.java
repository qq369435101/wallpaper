package com.company.wallpaper.utils;

import android.app.Activity;

//import com.company.wallpaper.ui.goods.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2018/10/26.
 * 用于商品详情页activity控制
 */

public class GoodsDetailActivityManager {
    private List<Activity> activityList = new ArrayList<>();
    private static GoodsDetailActivityManager instance;

    private GoodsDetailActivityManager() {
    }

    public static GoodsDetailActivityManager getInstance() {
        if (instance == null) {
//            synchronized (GoodsDetailActivity.class) {
//                instance = new GoodsDetailActivityManager();
//            }
        }
        return instance;
    }
    //连续打开超过3个商品，则关闭第一个，避免造成页面打开过多导致手机卡顿
    public void putActivity(Activity activity) {
        activityList.add(activity);
        if (activityList.size() > 3) {
            activityList.get(0).finish();
//            activityList.remove(0);
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
        }
        activityList.remove(activity);
        if (activityList.size() == 0) {
            instance = null;
        }
    }
    //退出所有商品详情页
    public void removeAllActivity() {
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
    }
}
