package com.ysy.commonlib.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;


/**
 * Created by yushengyang.
 * Date: 2018/12/5.
 */

public class WeightUtils {
    public static AlertDialog usefulDialog(Context context,int LayoutId,int style,int closeId,int cancelId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, style);
        View inflate = ((Activity) context).getLayoutInflater().inflate(LayoutId, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setContentView(inflate);
        inflate.findViewById(closeId).setOnClickListener(v -> alertDialog.dismiss());
        inflate.findViewById(cancelId).setOnClickListener(v -> alertDialog.dismiss());
        return alertDialog;
    }

    public static <T extends ViewDataBinding> T usefulDialogDatabinding(Context context, int LayoutId, int style, int closeId, int cancelId,T t){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, style);
        t= DataBindingUtil.inflate(((Activity) context).getLayoutInflater(),LayoutId,null,false);
        View inflate = t.getRoot();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setContentView(inflate);
        inflate.findViewById(closeId).setOnClickListener(v -> alertDialog.dismiss());
        inflate.findViewById(cancelId).setOnClickListener(v -> alertDialog.dismiss());
        return t;
    }
}
