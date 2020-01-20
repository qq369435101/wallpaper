package com.company.wallpaper.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.company.wallpaper.databinding.LayoutPhotoDialogBinding;

import com.company.wallpaper.R;
import com.company.wallpaper.utils.FileUtils;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.utils.wechatutils.WechatLoginHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
//import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.opensdk.modelmsg.WXImageObject;
//import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.ysy.commonlib.utils.StringUtils;

import java.util.concurrent.ExecutionException;

import static com.ysy.commonlib.utils.DevicesUtils.FlymeSetStatusBarLightMode;
import static com.ysy.commonlib.utils.DevicesUtils.MIUISetStatusBarLightMode;

/**
 * Created by yushengyang.
 * Date: 2018/12/29.
 */

public class PhotoDialog extends Dialog {
    Context context;
    LayoutPhotoDialogBinding databing;
    private String imgUrl;

    public PhotoDialog(@NonNull Context context, String imgUrl) {
        super(context, R.style.BottomDialog);
        this.context = context;
        this.imgUrl = imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databing = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.layout_photo_dialog, null, false);
        setCancelable(true);
        setContentView(databing.getRoot());
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        transparencyBar(getWindow());
        databing.tvCancel.setOnClickListener(v -> dismiss());
        databing.tvSave.setOnClickListener(v -> {
            RxPermissions rxPermissions = new RxPermissions(((Activity) context));
            rxPermissions.request(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_COARSE_LOCATION
            ).subscribe(granted -> {
                if (granted) {
                    savePhotoToLocal();
                } else {
                    ToastUtils.showToast("请前往权限管理开启相机和相册相关权限！");
                }
            });

        });
        databing.tvShareWechat.setOnClickListener(v1 -> shareToWechat(true));
        databing.tvShareCircle.setOnClickListener(v -> shareToWechat(false));

    }

    private void transparencyBar(Window window) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (MIUISetStatusBarLightMode(window, true)) {
                window.setFlags(67108864, 67108864);
            } else if (FlymeSetStatusBarLightMode(window, true)) {
                window.setFlags(67108864, 67108864);
            } else if (Build.VERSION.SDK_INT >= 23) {
                window.clearFlags(201326592);
                window.getDecorView().setSystemUiVisibility(1280);
                window.addFlags(-2147483648);
                window.setStatusBarColor(0);
            }
        }
    }

    private void shareToWechat(boolean isFriend) {
//        new Thread(() -> {
//            try {
//                Bitmap bitmap = Glide.with(context).asBitmap().load(imgUrl).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
//                WXImageObject imgObj = new WXImageObject(bitmap);
//                WXMediaMessage msg = new WXMediaMessage();
//                msg.mediaObject = imgObj;
//                Bitmap thumBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
//                msg.thumbData = FileUtils.bitmapToByteArray(thumBitmap, true);
//                SendMessageToWX.Req req = new SendMessageToWX.Req();
//                req.transaction = StringUtils.buildTransaction("img");
//                req.message = msg;
//                if (isFriend) {
//                    req.scene = SendMessageToWX.Req.WXSceneSession;
//                } else {
//                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
//                }
//                ((Activity) context).runOnUiThread(() -> WechatLoginHelper.doShare(context, req));
//                bitmap.recycle();
//                thumBitmap.recycle();
//                PhotoDialog.this.dismiss();
//            } catch (ExecutionException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();

    }


    private void savePhotoToLocal() {
//        ViewGroup containerTemp = (ViewGroup) mPager.findViewWithTag(mPager.getCurrentItem());
//        if (containerTemp == null) {
//            return;
//        }
//        PhotoView photoViewTemp = (PhotoView) containerTemp.getChildAt(0);
//            BitmapDrawable glideBitmapDrawable = (BitmapDrawable) photoViewTemp.getDrawable();
        if (imgUrl == null) {
            return;
        }
        FileUtils.savePhoto(context, imgUrl, new FileUtils.SaveResultCallback() {
            @Override
            public void onSavedSuccess() {
                ((Activity) context).runOnUiThread(() -> {
                    ToastUtils.showToast("保存成功");
                    PhotoDialog.this.dismiss();
                });
            }

            @Override
            public void onSavedFailed() {
                ((Activity) context).runOnUiThread(() -> {
                    ToastUtils.showToast("保存失败");
                    PhotoDialog.this.dismiss();
                });
            }
        });
    }
}
