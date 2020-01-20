package com.company.wallpaper.eventhandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.company.wallpaper.ui.WebViewActivity;
import com.company.wallpaper.ui.home.HomeActivity;
import com.company.wallpaper.view.MyAlertDialog;
import com.ysy.commonlib.base.BaseEventHandler;

/**
 * Created by yushengyang.
 * Date: 2018/12/12.
 */

public class AppBaseEventHandler extends BaseEventHandler {
    public MyAlertDialog defaultDialog;
    public EventListener eventListener;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void ToHomeActivity(View view, int type) {
        Intent intent = new Intent(view.getContext(), HomeActivity.class);

        if (type == 1) {
            intent.putExtra("isToFirst", true);
        }
        if (type == 2) {
            intent.putExtra("isToSecond", true);
        }
        if (type == 3) {
            intent.putExtra("isToThird", true);
        }
        if (type == 4) {
            intent.putExtra("isToFourth", true);
        }
        if (type == 5) {
            intent.putExtra("isToFifth", true);
        }
        view.getContext().startActivity(intent);
        ((Activity) view.getContext()).finish();
    }


    /**
     * 默认弹窗
     *
     * @param content
     * @param context
     * @param listener
     */
    public void defaultDialog(String content, Context context, View.OnClickListener listener) {

        if (defaultDialog != null) {
            defaultDialog.dismiss();
        } else {
            defaultDialog = new MyAlertDialog(context, content);
        }
        defaultDialog.setPositiveListener(listener);
        defaultDialog.show();
    }

    public interface EventListener {
        void handleEvent(int type);
    }




}
