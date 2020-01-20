package com.company.wallpaper.utils.wechatutils;

import android.content.Context;
import android.content.Intent;

import com.company.wallpaper.repository.Response_WechatUserInfo;
import com.company.wallpaper.utils.ToastUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ysy.commonlib.utils.StringUtils;


/**
 * Created by lixingjun on 7/31/16.
 */
public class WechatLoginHelper {
    // APP_ID 替换为你的应用从官方网站申请到的合法appId
//    public static String APP_ID = "wx0658b71952fd676e";
    public static String APP_ID = "wx65e53d1941265fcc";
    //    public static String SECRET_KEY = "60c11edf728e405199b2db52127b29ff";
    public static String SECRET_KEY = "923da17035359d9637c042b74ab4b4f6";

    public static IWXAPI weixinAPI = null;
    public static String openid = null;
    public static WechatLoginCallBack loginCallBack = null;

    private WechatLoginHelper() {
    }

    public static void doShare(Context context, SendMessageToWX.Req req) {
        weixinAPI = WXAPIFactory.createWXAPI(context, null);
        weixinAPI.registerApp(WechatLoginHelper.APP_ID);
        if (isWXAppInstalledAndSupported()) {
            weixinAPI.sendReq(req);
        } else {
            ToastUtils.showToast("未检测到微信应用，请使用其他登录方式！");
        }
    }

    public static void WechatShare(Context context, String webUrl, String title, String desc, boolean isFriend) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
//        Bitmap bitmap = FileUtils.getBitmap(context, R.mipmap.ic_launcher);
//        msg.thumbData = FileUtils.bitmap2Bytes(bitmap, 32);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = StringUtils.buildTransaction("webpage");
        req.message = msg;
        if (isFriend) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        WechatLoginHelper.doShare(context, req);
//        bitmap.recycle();
    }
    public static void handleIntent(Context context, Intent intent) {
        weixinAPI = WXAPIFactory.createWXAPI(context, WechatLoginHelper.APP_ID, false);
        weixinAPI.registerApp(WechatLoginHelper.APP_ID);
        WechatLoginHelper.weixinAPI.handleIntent(intent, (IWXAPIEventHandler) context);
    }

    public static boolean doLogin(Context context, WechatLoginCallBack callBack) {
        weixinAPI = WXAPIFactory.createWXAPI(context, APP_ID, true);
        weixinAPI.registerApp(APP_ID);
        if (isWXAppInstalledAndSupported()) {
            loginCallBack = callBack;
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "carjob_wx_login";
            weixinAPI.sendReq(req);//执行完毕这句话之后，会在WXEntryActivity回调}
            return true;
        } else {

            return false;
        }
    }

    private static boolean isWXAppInstalledAndSupported() {
        boolean sIsWXAppInstalledAndSupported = weixinAPI.isWXAppInstalled();
        return sIsWXAppInstalledAndSupported;
    }


    public interface WechatLoginCallBack {
        void onSuccess(Response_WechatUserInfo info);


        void onFailure();
    }


}
