package com.company.wallpaper.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.company.wallpaper.R;
import com.company.wallpaper.utils.DynamicTimeFormat;
import com.company.wallpaper.utils.ShareUtils;
import com.company.wallpaper.utils.ad.TTAdManagerHolder;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.ysy.commonlib.utils.StethoUtils;

//import com.alibaba.android.arouter.launcher.ARouter;


/**
 * Created by yushengyang.
 * Date: 2018/8/23.
 */

public class MyApplication extends Application {
    public static int code = 1;
    private static MyApplication myApplication;
    protected static int mainThreadId;
    protected static Handler handler;
    public static boolean isGiftOrder = false;
    public static int payType = 0;
    public static String Cookie = "";

    static {
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer((context, layout) -> {
            //全局设置（优先级最低）
            layout.setEnableLoadMore(false);
            layout.setEnableAutoLoadMore(true);
            layout.setEnableOverScrollDrag(true);
            layout.setEnableOverScrollBounce(true);
            layout.setEnableLoadMoreWhenContentNotFull(true);
            layout.setEnableScrollContentWhenRefreshed(true);
        });
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
            layout.setPrimaryColorsId(R.color.withe, android.R.color.black);
            return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        init();
    }

    private void init() {
        initStetho();
        ShareUtils.init(this);
        LeakCanary.install(this);
        mainThreadId = android.os.Process.myTid();
        handler = new Handler();
        MultiDex.install(this);
        // 初始化SDK
        UMConfigure.init(this, "5ddcce03570df38291000316", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
//        if (AppConfig.ONLINE_SERVER == AppConfig.RELEASE) {
//            CrashReport.initCrashReport(getApplicationContext());
//        }
//        ARouter.init(this);
        initAd();
    }

    private void initAd() {
        TTAdManagerHolder.init(this);

    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    private void initStetho() {
        StethoUtils.init(this);
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(new LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("okhttp")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
