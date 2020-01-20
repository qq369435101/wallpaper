package com.company.wallpaper.ui.home;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.TextView;

import com.company.wallpaper.R;
import com.company.wallpaper.adapter.UserInfoItemAdapter;
import com.company.wallpaper.app.BaseFragment;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.bean.UserInfoItemBean;
import com.company.wallpaper.databinding.FragmentUserinfoBinding;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.ui.LoginActivity;
import com.company.wallpaper.ui.NewActivity;
import com.company.wallpaper.ui.PaperListActivity;
import com.company.wallpaper.utils.GlideCacheUtil;
import com.company.wallpaper.utils.ShareUtils;
import com.company.wallpaper.utils.ToastUtils;
import com.company.wallpaper.view.PhotoPickDialog;
import com.company.wallpaper.viewmodel.HomeActivityViewModel;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.sunfusheng.marqueeview.MarqueeView;
import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;
import com.ysy.commonlib.utils.DialogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

//import com.alibaba.android.arouter.launcher.ARouter;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends BaseFragment<FragmentUserinfoBinding> implements PhotoPickDialog.PhotoListener {
    File tempFile;
    public static final int Photo_Request_Code = 300;
    private int[] icons = {R.drawable.icon_collect, R.drawable.icon_save, R.drawable.icon_share, R.drawable.icon_time};
    private String[] itemNames = {"收藏", "保存", "分享", "浏览"};
    private HomeActivityViewModel viewModel;

    public UserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        List<UserInfoItemBean> userInfoItemBeans = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            UserInfoItemBean bean = new UserInfoItemBean();
            bean.setResId(icons[i]);
            bean.setItemName(itemNames[i]);
            userInfoItemBeans.add(bean);
        }
        bindingView.rvUserinfo.setAdapter(new UserInfoItemAdapter(getActivity(), userInfoItemBeans));
        bindingView.rvUserinfo.addItemDecoration(new DividerItemDecoration(getActivity()));
        bindingView.refreshLayout.setEnableRefresh(false);
        bindingView.tvSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });
        bindingView.ivHead.setOnClickListener(v -> {
            if (ShareUtils.getUserInfo() == null) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                new PhotoPickDialog(v.getContext(), Photo_Request_Code, this).show();
            }

        });
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        getData();

    }

    public void getData() {
        viewModel.news(new onResponseListener<List<PaperTypeBean>>() {
            @Override
            public void onSuccess(List<PaperTypeBean> paperTypeBeans) {
                ArrayList<String> list = new ArrayList<>();
                for (PaperTypeBean paperTypeBean : paperTypeBeans) {
                    list.add(paperTypeBean.getTypeName());
                }
                bindingView.marqueeView.startWithList(list, R.anim.anim_bottom_in, R.anim.anim_top_out);
                bindingView.marqueeView.setOnItemClickListener((position, textView) -> PaperListActivity.toPagerListActivity(paperTypeBeans.get(position).getTypeId(), paperTypeBeans.get(position).getTypeName(), 1, getActivity()));
            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });

        bindingView.tvMessage.setOnClickListener(v -> NewActivity.ToNewsListActivity(getActivity()));
    }

    @Override
    public int setContent() {
        return R.layout.fragment_userinfo;
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        bindingView.setBean(ShareUtils.getUserInfo());
    }


    private class DividerItemDecoration extends Y_DividerItemDecoration {

        private DividerItemDecoration(Context context) {
            super(context);
        }

        @Override
        public Y_Divider getDivider(int itemPosition) {
            Y_Divider divider = null;
            switch (itemPosition % 3) {
                case 0:
                case 1:
                    if (itemPosition < 3) {
                        divider = new Y_DividerBuilder()
                                .setRightSideLine(true, 0xffEBEBEB, 1, 0, 0)
                                .setBottomSideLine(true, 0xffEBEBEB, 1, 0, 0)
                                .create();
                    } else if (icons.length - 1 - itemPosition < (icons.length % 3)) {
                        divider = new Y_DividerBuilder()
                                .setRightSideLine(true, 0xffEBEBEB, 1, 0, 0)
                                .create();
                    } else {
                        //每一行第一个和第二个显示rignt和bottom
                        divider = new Y_DividerBuilder()
                                .setRightSideLine(true, 0xffEBEBEB, 1, 0, 0)
                                .setBottomSideLine(true, 0xffEBEBEB, 1, 0, 0)
                                .create();
                    }
                    break;
                case 2:
                    if (itemPosition < 3) {
                        divider = new Y_DividerBuilder()
                                .setBottomSideLine(true, 0xffEBEBEB, 1, 0, 0)
                                .create();
                    } else if (icons.length - 1 - itemPosition < (icons.length % 3)) {
                        divider = new Y_DividerBuilder()
                                .create();
                    } else {
                        //最后一个只显示bottom
                        divider = new Y_DividerBuilder()
                                .setBottomSideLine(true, 0xffEBEBEB, 1, 0, 0)
                                .create();
                    }
                    break;
                default:
                    break;
            }
            return divider;
        }
    }

    @Override
    public void takePhoto(int requestCode) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, createCameraTempFile());
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, requestCode + 100);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     */
    private Uri createCameraTempFile() {
        tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), System.currentTimeMillis() + ".jpg");//拍照文件的路径
        return FileProvider.getUriForFile(getActivity(), "com.company.wallpaper.ImgProvider", tempFile);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Photo_Request_Code) {
                DisplayImg(data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0));
            }
            if (requestCode == Photo_Request_Code + 100) {
                Luban.with(getActivity()).load(tempFile).ignoreBy(300).setTargetDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()).filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"))).setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        DisplayImg(tempFile.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showToast("图像上传失败！请重试");
                    }
                }).launch();

            }
        }
    }

    private void DisplayImg(String path) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        //2.获取图片，创建请求体
        File file = new File(path);
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);//表单类型
        builder.addFormDataPart("file", file.getName(), body);
        builder.addFormDataPart("userId",ShareUtils.getUserInfo().getUserId()+"");
        QMUITipDialog tipDialog = DialogUtils.showLoadingDialog(getActivity(), "上传中。。。");
        tipDialog.show();
        viewModel.Upload(builder.build().parts(), new onResponseListener<String>() {
            @Override
            public void onSuccess(String s) {
                GlideCacheUtil.LoadImage(getActivity(), bindingView.ivHead, s);
                tipDialog.dismiss();

            }

            @Override
            public void onFailed(Throwable throwable) {
                tipDialog.dismiss();
                DialogUtils.showFailedDialog(getActivity(), bindingView.ivHead, throwable.getMessage());
            }
        });
    }
}
