package com.company.wallpaper.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.company.wallpaper.R;
import com.company.wallpaper.app.MVVMActivity;
import com.company.wallpaper.databinding.ActivityRegisteBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.utils.PatternUtils;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.ysy.commonlib.base.BaseEventHandler;
import com.ysy.commonlib.base.TResponse;
import com.ysy.commonlib.utils.DialogUtils;
import com.ysy.commonlib.utils.StringUtils;

//import com.alibaba.android.arouter.facade.annotation.Route;

//@Route(path = "/user/register")
public class RegisteActivity extends MVVMActivity<ActivityRegisteBinding, HomeActivityViewModel> implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        showContentView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_msg: {
                if (PatternUtils.isChinaPhoneLegal(bindingView.etUsername.getText().toString())) {
                    QMUITipDialog tipDialog = DialogUtils.showLoadingDialog(v.getContext(), "发送中~");
                    tipDialog.show();
                    mViewModel.sendCode(bindingView.etUsername.getText().toString(), new onResponseListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            tipDialog.dismiss();
                            new BaseEventHandler().codeTime((TextView) v, RegisteActivity.this::onClick);
                            DialogUtils.showSuccessDialog(v.getContext(), v, "发送成功~");
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            tipDialog.dismiss();
                            DialogUtils.showFailedDialog(v.getContext(), v, throwable.getMessage());
                        }
                    });
                } else {
                    DialogUtils.showFailedDialog(v.getContext(), v, "请输入正确的电话号码");
                }

                break;
            }
            case R.id.btn_submit: {
                if (bindingView.cbUserRule.isChecked()) {
                    if (PatternUtils.isChinaPhoneLegal(bindingView.etUsername.getText().toString())) {
                        if (!StringUtils.isEmptyString(bindingView.etMessage.getText().toString())) {
                            if (!StringUtils.isEmptyString(bindingView.etPassword.getText().toString()) && !StringUtils.isEmptyString(bindingView.etPasswordConfirm.getText().toString())) {
                                if (bindingView.etPassword.getText().toString().equals(bindingView.etPasswordConfirm.getText().toString())) {
                                    mViewModel.register(bindingView.etUsername.getText().toString(), bindingView.etPassword.getText().toString(), bindingView.etPasswordConfirm.getText().toString(), bindingView.etMessage.getText().toString(), new onResponseListener<TResponse>() {
                                        @Override
                                        public void onSuccess(TResponse tResponse) {
                                            ToastUtils.showToast(tResponse.getMsg());
                                            if (tResponse.getMsg().equals("操作成功")) {
                                                onBackPressed();
                                            }
                                        }

                                        @Override
                                        public void onFailed(Throwable throwable) {
                                            DialogUtils.showFailedDialog(v.getContext(), v, throwable.getMessage());
                                        }
                                    });
                                } else {
                                    DialogUtils.showFailedDialog(v.getContext(), v, "两次密码输入不一致~");
                                }
                            } else {
                                DialogUtils.showFailedDialog(v.getContext(), v, "请输入密码");
                            }

                        } else {
                            DialogUtils.showFailedDialog(v.getContext(), v, "请输入验证码");
                        }
                    } else {
                        DialogUtils.showFailedDialog(v.getContext(), v, "请输入正确的电话号码");
                    }
                } else {
                    DialogUtils.showFailedDialog(v.getContext(), v, "请阅读用户协议~");
                }

                break;
            }
            case R.id.tv_register_rule:{
                WebViewActivity.toWebView(RegisteActivity.this,"隐私协议","http://www.rioses.top/?act=dyPrivacy");
                break;
            }
            case R.id.tv_register_rule1:{
                WebViewActivity.toWebView(RegisteActivity.this,"注册协议","http://www.rioses.top/?act=zyPrivacy");
                break;
            }

        }
    }
}
