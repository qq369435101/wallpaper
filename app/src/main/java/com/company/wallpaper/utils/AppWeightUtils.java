package com.company.wallpaper.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.company.wallpaper.ui.home.HomeActivity;

/**
 * Created by yushengyang.
 * Date: 2018/12/20.
 */

public class AppWeightUtils {
    public static AlertDialog toVipGuideActivity(Context context, int LayoutId, int style, int closeId, int positiveId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, style);
        View inflate = ((Activity) context).getLayoutInflater().inflate(LayoutId, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setContentView(inflate);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnDismissListener(dialog -> {
            Intent intent=new Intent(context, HomeActivity.class);
            intent.putExtra("isToThird", true);
            context.startActivity(intent);
        });
        inflate.findViewById(closeId).setOnClickListener(v -> alertDialog.dismiss());
        inflate.findViewById(positiveId).setOnClickListener(v -> alertDialog.dismiss());
        return alertDialog;
    }
}
