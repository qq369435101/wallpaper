package com.ysy.commonlib.utils;

import android.content.Context;
import android.view.View;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.ref.WeakReference;

/**
 * Created by yushengyang.
 * Date: 2019/3/20.
 */

public class DialogUtils {
    /**
     * @param context 传入applicationContext即可
     */
    public static QMUITipDialog showLoadingDialog(Context context) {
        return showLoadingDialog(context, "加载中...");
    }

    /**
     * @param context
     * @param content 显示文字内容
     */
    public static QMUITipDialog showLoadingDialog(Context context, String content) {
        WeakReference<Context> mContext = new WeakReference<>(context);
        return new QMUITipDialog.Builder(mContext.get())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(content)
                .create();
    }


    /**
     * @param context
     * @param content 显示文字内容
     */
    public static void showSuccessDialog(Context context, View view, String content, onDialogCloseListener listener) {
        WeakReference<Context> mContext = new WeakReference<>(context);
        WeakReference<View> mView = new WeakReference<>(view);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(mContext.get())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(content)
                .create();
        tipDialog.show();
        mView.get().postDelayed(() -> {
            tipDialog.dismiss();
            if (listener != null) {
                listener.DialogClose();
            }
        }, 1500);
    }

    public static void showSuccessDialog(Context context, View view, String content) {
        showSuccessDialog(context, view, content, null);
    }

    public static void showSuccessDialog(Context context, View view) {
        showSuccessDialog(context, view, "已完成", null);
    }

    /**
     * @param context
     * @param content 显示文字内容
     */
    public static void showFailedDialog(Context context, View view, String content, long durationTime, onDialogCloseListener listener) {
        if (durationTime == -1) {
            if (content.length() < 10) {
                durationTime = 1500;
            } else if (content.length() < 20) {
                durationTime = 2000;
            } else {
                durationTime = 3000;
            }
        }
        WeakReference<Context> mContext = new WeakReference<>(context);
        WeakReference<View> mView = new WeakReference<>(view);
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(mContext.get())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(content)
                .create();
        tipDialog.show();

        mView.get().postDelayed(() -> {
            tipDialog.dismiss();
            if (listener != null) {
                listener.DialogClose();
            }
        }, durationTime);
    }

    public static void showFailedDialog(Context context, View view, String content) {
        showFailedDialog(context, view, content, -1, null);
    }

    public static void showFailedDialog(Context context, View view, String content, onDialogCloseListener listener) {
        showFailedDialog(context, view, content, -1, listener);
    }

    public static void showFailedDialog(Context context, View view) {
        showFailedDialog(context, view, "操作失败");
    }

    public interface onDialogCloseListener {
        void DialogClose();
    }
}
