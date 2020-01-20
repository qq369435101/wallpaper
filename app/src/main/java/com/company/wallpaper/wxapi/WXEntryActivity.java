package com.company.wallpaper.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.repository.WechatShareLoginRepository;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.utils.wechatutils.WechatLoginHelper;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.HashMap;


public class WXEntryActivity extends FragmentActivity implements IWXAPIEventHandler {

    private Bundle bundle;

    //这个实体类是我自定义的实体类，用来保存第三方的数据的实体类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		AppManager. getAppManager().addActivity(WXEntryActivity. this);
        WechatLoginHelper.handleIntent(this, getIntent());  //必须调用此句话
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WechatLoginHelper.handleIntent(this, intent);//必须调用此句话
    }

    @Override
    public void onReq(BaseReq req) {
        System.out.println();
    }


    /**
     * Title: onResp
     * <p/>
     * API：https://open.weixin.qq.com/ cgi- bin/showdocument ?action=dir_list&t=resource/res_list&verify=1&id=open1419317853 &lang=zh_CN
     * Description:在此处得到Code之后调用https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * 获取到token和openID。之后再调用https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID 获取用户个人信息
     *
     * @param arg0
     * @see
     */
    @Override
    public void onResp(BaseResp arg0) {
        bundle = getIntent().getExtras();
        SendAuth.Resp resp = new SendAuth.Resp(bundle);
        System.out.println("***************************************************************=" + resp.errCode);
        //获取到code之后，需要调用接口获取到access_token
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            if (resp.code != null) {
                HashMap<String, String> map = new HashMap<>();
                map.put("appid", WechatLoginHelper.APP_ID);
                map.put("secret", WechatLoginHelper.SECRET_KEY);
                map.put("code", resp.code);
                map.put("grant_type", "authorization_code");
                new WechatShareLoginRepository().getUseInfo(map, new onResponseListener() {
                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFailed(Throwable throwable) {
//                        ToastUtils.showToast("登录失败！");
                        WechatLoginHelper.loginCallBack.onFailure();
                        onBackPressed();
                    }
                });
                finish();
            } else {
                ToastUtils.showToast("分享成功！");
                finish();
            }
        } else {
            WechatLoginHelper.loginCallBack.onFailure();
            finish();
        }
    }


}