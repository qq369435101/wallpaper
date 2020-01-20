package com.company.wallpaper.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.bean.UserInfoBean;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.repository.HomePageRepository;
import com.ysy.commonlib.base.TResponse;

import java.util.List;

import okhttp3.MultipartBody;

/**
 * Created by yushengyang.
 * Date: 2018/11/26.
 */

public class HomeActivityViewModel extends ViewModel {
    private HomePageRepository repository = new HomePageRepository();

    public void HomePageInfo(int pageNum, onResponseListener<TResponse<List<PaperTypeBean>>> listener) {
        repository.HomePageInfo(pageNum, listener);
    }

    public void pageType(onResponseListener<List<PaperTypeBean>> listener) {
        repository.pageType(listener);
    }

    public void papertypesun(int typeId, onResponseListener<List<PaperTypeBean>> listener) {
        repository.papertypesun(typeId, listener);
    }

    public void typecategory(int typeId, onResponseListener<List<PaperTypeBean>> listener) {
        repository.typecategory(typeId, listener);
    }

    public void sendCode(String phone, onResponseListener<String> listener) {
        repository.sendCode(phone, listener);
    }


    public void loginCode(String phone, onResponseListener<String> listener) {
        repository.loginCode(phone, listener);
    }

    public void version(onResponseListener<TResponse> listener) {
        repository.version(listener);
    }

    public void register(String userName, String password, String passwordb, String code, onResponseListener<TResponse> listener) {
        repository.register(userName, password, passwordb, code, listener);
    }

    public void suncategory(int paperType, String paperName, int PageNum, int PageSize, onResponseListener<List<PaperTypeBean>> listener) {
        repository.suncategory(paperType, paperName, PageNum, PageSize, listener);
    }

    public void checkcollect(int paperId, int typeId, int videoId, onResponseListener<List<PaperTypeBean>> listener) {
        repository.checkcollect(paperId, typeId, videoId, listener);
    }

    public void checkcollect(int typeId, int videoId, onResponseListener<List<PaperTypeBean>> listener) {
        repository.checkcollect(typeId, videoId, listener);
    }

    public void checkcollect(String typeName, int paperId, int videoId, int typeId, onResponseListener<List<PaperTypeBean>> listener) {
        repository.checkcollect(paperId, typeName, videoId, typeId, listener);
    }

    public void checkcollect(int paperId, String formType, int videoId, int typeId, onResponseListener<List<PaperTypeBean>> listener) {
        repository.checkcollect(formType, paperId, videoId, typeId, listener);
    }

    public void login(String loginName, String password, onResponseListener<UserInfoBean> listener) {
        repository.login(loginName, password, listener);
    }

    public void loginSms(String loginName, String sendCode, onResponseListener<UserInfoBean> listener) {
        repository.loginSms(loginName, sendCode, listener);
    }

    public void videotype(onResponseListener<List<PaperTypeBean>> listener) {
        repository.videotype(listener);
    }

    public void video(int typeId, int PageNum, onResponseListener<List<PaperTypeBean>> listener) {
        repository.video(typeId, PageNum, listener);
    }


    public void typehot(onResponseListener<List<PaperTypeBean>> listener) {
        repository.typehot(listener);
    }

    public void news(onResponseListener<List<PaperTypeBean>> listener) {
        repository.news(listener);
    }

    public void homecategory(int typeid, onResponseListener<List<PaperTypeBean>> listener) {
        repository.homecategory(typeid, listener);
    }

    public void paperquery(int pageNum, int pageSize, String typeName, onResponseListener<List<PaperTypeBean>> listener) {
        repository.paperquery(pageNum, pageSize, typeName, listener);
    }

    public void download(int paperId, onResponseListener<String> listener) {
        repository.download(paperId, listener);
    }

    public void collect(int paperId, int videoId, onResponseListener listener) {
        repository.collect(paperId, videoId, listener);
    }

    public void deletecollect(int paperId, int videoId, onResponseListener listener) {
        repository.deletecollect(paperId, videoId, listener);
    }

    public void Upload(List<MultipartBody.Part> partLis, onResponseListener<String> listener) {
        repository.Upload(partLis, listener);
    }

    public void collectlist(int pageNum, int pageSize, onResponseListener<List<PaperTypeBean>> listener) {
        repository.collectlist(pageNum, pageSize, listener);
    }

    public void downloadlist(int pageNum, int pageSize, onResponseListener<List<PaperTypeBean>> listener) {
        repository.downloadlist(pageNum, pageSize, listener);
    }

    public void sharelist(int pageNum, int pageSize, onResponseListener<List<PaperTypeBean>> listener) {
        repository.sharelist(pageNum, pageSize, listener);
    }

    public void browselist(int pageNum, int pageSize, onResponseListener<List<PaperTypeBean>> listener) {
        repository.browselist(pageNum, pageSize, listener);
    }

    public void share(int paperId, int videoId) {
        repository.share(paperId, videoId);
    }

    public void weChat(String wechatId, String wechatName, String wechatUrl, onResponseListener<TResponse<UserInfoBean>> listener) {
        repository.weChat(wechatId, wechatName, wechatUrl, listener);
    }

    public void weChatCode(String phone, onResponseListener<String> listener) {
        repository.weChatCode(phone, listener);
    }

    public void weChatLogin(String password, String phoneNumber, String sendCode, String wechatId, onResponseListener<TResponse<UserInfoBean>> listener) {
        repository.weChatLogin(password, phoneNumber, sendCode, wechatId, listener);
    }

    public void deletebrowse(String paperId, String VideoId, onResponseListener listener) {
        repository.deletebrowse(paperId, VideoId, listener);
    }

    public void deleteshare(String paperId, String VideoId, onResponseListener listener) {
        repository.deleteshare(paperId, VideoId, listener);
    }

    public void deletecollects(String paperId, String VideoId, onResponseListener listener) {
        repository.deletecollects(paperId, VideoId, listener);
    }

    public void deletedownload(String paperId, String VideoId, onResponseListener listener) {
        repository.deletedownload(paperId, VideoId, listener);
    }
}
