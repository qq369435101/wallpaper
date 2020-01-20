package com.company.wallpaper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.company.wallpaper.R;
import com.company.wallpaper.app.MVVMActivity;
import com.company.wallpaper.bean.UserInfoBean;
import com.company.wallpaper.databinding.ActivityLoginBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.repository.Response_WechatUserInfo;
import com.company.wallpaper.utils.PatternUtils;
import com.company.wallpaper.utils.ShareUtils;
import com.company.wallpaper.utils.wechatutils.WechatLoginHelper;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.ysy.commonlib.base.BaseEventHandler;
import com.ysy.commonlib.base.TResponse;
import com.ysy.commonlib.utils.DialogUtils;


public class LoginActivity extends MVVMActivity<ActivityLoginBinding, HomeActivityViewModel> {
    private QMUITipDialog tipDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showContentView();
        bindingView.titleBar.getRightTextView().setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisteActivity.class)));
        bindingView.setIsMessage(false);
        bindingView.ivWechatLogin.setOnClickListener(view -> {
            if (!WechatLoginHelper.doLogin(view.getContext(), wechatLoginCallBack)) {
                tipDialog.dismiss();
                DialogUtils.showFailedDialog(view.getContext(), view, "未检测到微信应用，请安装微信后再进行登陆！");
            } else {
                tipDialog = DialogUtils.showLoadingDialog(view.getContext(), "登陆中。。。");
                tipDialog.show();
            }
        });
    }

    private WechatLoginHelper.WechatLoginCallBack wechatLoginCallBack = new WechatLoginHelper.WechatLoginCallBack() {
        @Override
        public void onSuccess(Response_WechatUserInfo info) {
            CheckInfo(info);
        }

        @Override
        public void onFailure() {
            tipDialog.dismiss();
            DialogUtils.showFailedDialog(LoginActivity.this, bindingView.btnSubmit, "需要您先同意微信授权，才能登录哦～");
        }
    };

    private void CheckInfo(Response_WechatUserInfo info) {
        mViewModel.weChat(info.getOpenid(), info.getNickname(), info.getHeadimgurl(), new onResponseListener<TResponse<UserInfoBean>>() {
            @Override
            public void onSuccess(TResponse<UserInfoBean> tResponse) {
                tipDialog.dismiss();
                ShareUtils.putUserInfo(tResponse.getData());
                DialogUtils.showSuccessDialog(LoginActivity.this, bindingView.titleBar, "登录成功！", () -> onBackPressed());
            }

            @Override
            public void onFailed(Throwable throwable) {
                tipDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, BindPhoneActivity.class);
                intent.putExtra("openId", info.getOpenid());
                startActivity(intent);
                finish();
            }
        });
    }

    public void changeWay(View view) {
        bindingView.setIsMessage(!bindingView.getIsMessage());
        if (bindingView.getIsMessage()) {
            bindingView.etPassCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            bindingView.etPassCode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void sendMsg(View v) {
        if (PatternUtils.isChinaPhoneLegal(bindingView.etPhone.getText().toString())) {
            QMUITipDialog tipDialog = DialogUtils.showLoadingDialog(v.getContext(), "发送中~");
            tipDialog.show();
            mViewModel.loginCode(bindingView.etPhone.getText().toString(), new onResponseListener<String>() {
                @Override
                public void onSuccess(String s) {
                    tipDialog.dismiss();
                    new BaseEventHandler().codeTime((TextView) v, LoginActivity.this::sendMsg);
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
    }

    public void Login(View v) {
        if (bindingView.getIsMessage()) {
            if (PatternUtils.isChinaPhoneLegal(bindingView.etPhone.getText().toString())) {
                if (!TextUtils.isEmpty(bindingView.etPassCode.getText().toString())) {
                    QMUITipDialog tipDialog = DialogUtils.showLoadingDialog(v.getContext(), "登录中~");
                    tipDialog.show();
                    mViewModel.loginSms(bindingView.etPhone.getText().toString(), bindingView.etPassCode.getText().toString(), new onResponseListener<UserInfoBean>() {
                        @Override
                        public void onSuccess(UserInfoBean userInfoBean) {
                            tipDialog.dismiss();
                            ShareUtils.putUserInfo(userInfoBean);
                            DialogUtils.showSuccessDialog(LoginActivity.this, v, "登录成功！", () -> onBackPressed());
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            tipDialog.dismiss();
                            DialogUtils.showFailedDialog(LoginActivity.this, v, throwable.getMessage()
                            );
                        }
                    });
                } else {
                    DialogUtils.showFailedDialog(v.getContext(), v, bindingView.getIsMessage() ? "请输入验证码~" : "请输入密码~");
                }
            } else {
                DialogUtils.showFailedDialog(v.getContext(), v, "请输入正确的电话号码");
            }
        } else {
            if (PatternUtils.isChinaPhoneLegal(bindingView.etPhone.getText().toString())) {
                if (!TextUtils.isEmpty(bindingView.etPassCode.getText().toString())) {
                    QMUITipDialog tipDialog = DialogUtils.showLoadingDialog(v.getContext(), "登录中~");
                    tipDialog.show();
                    mViewModel.login(bindingView.etPhone.getText().toString(), bindingView.etPassCode.getText().toString(), new onResponseListener<UserInfoBean>() {
                        @Override
                        public void onSuccess(UserInfoBean userInfoBean) {
                            tipDialog.dismiss();
                            ShareUtils.putUserInfo(userInfoBean);
                            DialogUtils.showSuccessDialog(LoginActivity.this, v, "登录成功！", () -> onBackPressed());
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            tipDialog.dismiss();
                            DialogUtils.showFailedDialog(LoginActivity.this, v, throwable.getMessage()
                            );
                        }
                    });
                } else {
                    DialogUtils.showFailedDialog(v.getContext(), v, bindingView.getIsMessage() ? "请输入验证码~" : "请输入密码~");
                }
            } else {
                DialogUtils.showFailedDialog(v.getContext(), v, "请输入正确的电话号码");
            }
        }
    }
}
