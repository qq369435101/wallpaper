package com.company.wallpaper.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.company.wallpaper.app.MyApplication;

/**
 * Created by ysy on 2018/1/15.
 */

public class ToastUtils {
    private static Toast mToast = null;

    /**
     * 显示一个toast提示
     *
     * @param resouceId toast字符串资源id
     */
//    public static void showToast(int resouceId) {
//        showToast(ResourcesUtils.getString(resouceId));
//    }

    /**
     * 显示一个toast提示
     *
     * @param text toast字符串
     */
    public static void showToast(String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示一个toast提示
     *
     * @param text     toast字符串
     * @param duration toast显示时间
     */
    public static void showToast(String text, int duration) {
        showToast(MyApplication.getInstance().getApplicationContext(), text, duration);
    }


    /**
     * 运行在主线程
     *
     * @param r 运行的Runnable对象
     */
    public static void runOnUIThread(Runnable r) {
        if (isRunOnUIThread()) {
            // 已经是主线程, 直接运行
            r.run();
        } else {
            // 如果是子线程, 借助handler让其运行在主线程
            getHandler().post(r);
        }
    }
    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return MyApplication.getHandler();
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    /**
     * 判断是否运行在主线程
     *
     * @return true：当前线程运行在主线程
     * fasle：当前线程没有运行在主线程
     */
    public static boolean isRunOnUIThread() {
        // 获取当前线程id, 如果当前线程id和主线程id相同, 那么当前就是主线程
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId()) {
            return true;
        }
        return false;
    }

    /**
     * 显示一个toast提示
     *
     * @param context  context 上下文对象
     * @param text     toast字符串
     * @param duration toast显示时间
     */
    public static void showToast(final Context context, final String text, final int duration) {
        Toast.makeText(context, text, duration).show();
//        /**
//         * 保证运行在主线程
//         */
//        runOnUIThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mToast == null) {
//                    mToast = Toast.makeText(context, text, duration);
//                } else {
//                    mToast.setText(text);
//                    mToast.setDuration(duration);
//                }
//                mToast.show();
//            }
//        });
    }

    public static void showLoginFirst(){
        showToast("请先登录！");
    }
}
