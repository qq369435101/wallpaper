package com.company.wallpaper.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.company.wallpaper.R;
import com.company.wallpaper.app.MyApplication;
import com.company.wallpaper.utils.AppWeightUtils;
import com.company.wallpaper.utils.ShareUtils;
import com.company.wallpaper.utils.wechatutils.WechatLoginHelper;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity
        implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    private int respCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_main2);
        api = WXAPIFactory.createWXAPI(this, WechatLoginHelper.APP_ID);
        api.handleIntent(getIntent(), this);
        api.registerApp(WechatLoginHelper.APP_ID);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {

    }


    @Override
    public void onResp(BaseResp resp) {
        respCode = resp.errCode;
//        if (resp.errCode == 0 && ShareUtils.getUserInfo().getUsertype() == 0 && MyApplication.isGiftOrder) {
//            showSuccessDialog();
//            return;
//        }
        finish();
    }

    //升级成功，显示弹窗
    public void showSuccessDialog() {
//        AlertDialog successDialog = AppWeightUtils.toVipGuideActivity(this, R.layout.layout_level_up_vip_guide_success, R.style.alert_dialog, R.id.iv_close, R.id.iv_cancel);
//        ((ImageView) successDialog.findViewById(R.id.iv_title)).setImageResource(R.mipmap.tit_vip_guide_level_up);
//        Window win = successDialog.getWindow();
//        win.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams lp = win.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        win.setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (respCode == 0 && !(MyApplication.isGiftOrder && ShareUtils.getUserInfo().getUsertype() == 0)) {
//            ToastUtils.showToast("支付成功");
//            OrderActivity.ToOrderActivity(this, true);
//        } else if (respCode == -1) {
//            ToastUtils.showToast("支付错误");
//            OrderActivity.ToOrderActivity(this, false);
//        } else if (respCode == -2) {
//            ToastUtils.showToast("取消支付");
//            OrderActivity.ToOrderActivity(this, false);
//        }
    }
}