package com.ysy.commonlib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019/3/26.
 */

public class PackageUtils {
    private static List<PackageInfo> cachedList;
    private static long lastDetectTime = 0;
    public static String QQBrowser = "com.tencent.mtt";
    public static String UCBrowser = " com.UCMobile";
    public static String ChromeBrowser = "com.android.chrome";
    public static String MozillaBrowser = "org.mozilla.firefox";
    public static String BaiDuBrowser = "com.baidu.browser.apps";
    public static String DefaultBrowser = "com.android.browser";

    private PackageUtils() {
    }

    public static String getBrowser(Context context) {
        if (isPackageAvailable(context, ChromeBrowser)) {
            return ChromeBrowser;
        } else if (isPackageAvailable(context, MozillaBrowser)) {
            return MozillaBrowser;
        } else if (isPackageAvailable(context, BaiDuBrowser)) {

            return BaiDuBrowser;
        } else if (isPackageAvailable(context, QQBrowser)) {

            return QQBrowser;
        } else if (isPackageAvailable(context, UCBrowser)) {
            return UCBrowser;
        } else {
            return DefaultBrowser;
        }
    }

    public static boolean isPackageAvailable(Context context, String pkgName) {
        List<PackageInfo> pinfo = getCachedList(context);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName.toLowerCase();
                if (pn.equals(pkgName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<PackageInfo> getCachedList(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取packagemanager
        if (cachedList == null || cachedList.size() == 0 || System.currentTimeMillis() - lastDetectTime > 60000 * 5) {
            cachedList = packageManager.getInstalledPackages(0);
            // 获取所有已安装程序的包信息
            lastDetectTime = System.currentTimeMillis();
        }
        return cachedList;
    }


}
