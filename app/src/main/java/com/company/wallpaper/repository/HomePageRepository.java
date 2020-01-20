package com.company.wallpaper.repository;


import com.company.wallpaper.AppConfig;
import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.bean.UserInfoBean;
import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.network.BaseRepository;
import com.company.wallpaper.network.RetrofitClient;
import com.company.wallpaper.network.service.HttpService;
import com.company.wallpaper.utils.ShareUtils;
import com.ysy.commonlib.base.TResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.MultipartBody;

/**
 * Created by yushengyang.
 * Date: 2018/11/12.
 */

public class HomePageRepository extends BaseRepository<Object> {
    public void HomePageInfo(int pageNum, onResponseListener<TResponse<List<PaperTypeBean>>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).paperlist(pageNum, AppConfig.DefaultPageSize), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse), listener::onFailed);
    }

    public void pageType(onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).papertype(), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void papertypesun(int typeId, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).papertypesun(typeId), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void typecategory(int typeId, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).typecategory(typeId), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void sendCode(String phone, onResponseListener<String> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).sendCode(phone), stringTResponse -> listener.onSuccess(stringTResponse.getData()), listener::onFailed);
    }

    public void loginCode(String phone, onResponseListener<String> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).loginCode(phone), stringTResponse -> listener.onSuccess(stringTResponse.getData()), listener::onFailed);
    }

    public void version(onResponseListener<TResponse> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).version(), tResponse -> listener.onSuccess(tResponse), throwable -> listener.onFailed(throwable));
    }

    public void register(String userName, String password, String passwordb, String code, onResponseListener<TResponse> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).register(userName, password, passwordb, code), listener::onSuccess, listener::onFailed);
    }

    public void suncategory(int paperType, String paperName, int PageNum, int PageSize, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).suncategory(PageNum, PageSize, paperType, paperName), listTResponse -> listener.onSuccess(listTResponse.getData()), listener::onFailed);
    }

    public void checkcollect(int paperId, int typeId, int videoId, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).checkcollect(paperId, typeId, ShareUtils.getUserInfo() == null ? 0 : ShareUtils.getUserInfo().getUserId(), videoId), listTResponse -> listener.onSuccess(listTResponse.getData()), listener::onFailed);
    }

    public void checkcollect(int typeId, int videoId, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).checkcollect(typeId, ShareUtils.getUserInfo() != null ? ShareUtils.getUserInfo().getUserId() : 0, videoId), listTResponse -> listener.onSuccess(listTResponse.getData()), listener::onFailed);
    }

    public void checkcollect(int paperId, String typeName, int videoId, int typeId, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).checkcollect(paperId, typeName, ShareUtils.getUserInfo() != null ? ShareUtils.getUserInfo().getUserId() : 0, videoId, typeId), listTResponse -> listener.onSuccess(listTResponse.getData()), listener::onFailed);
    }

    public void checkcollect(String formType, int paperId, int videoId, int typeId, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).checkcollect(formType, paperId, ShareUtils.getUserInfo() != null ? ShareUtils.getUserInfo().getUserId() : 0, videoId, typeId), listTResponse -> listener.onSuccess(listTResponse.getData()), listener::onFailed);
    }

    public void login(String loginName, String password, onResponseListener<UserInfoBean> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("loginName", loginName);
        map.put("password", password);
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).login(map), listTResponse -> listener.onSuccess(listTResponse.getData()), listener::onFailed);
    }

    public void loginSms(String loginName, String sendCode, onResponseListener<UserInfoBean> listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("loginName", loginName);
        map.put("sendCode", sendCode);
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).loginSms(map), listTResponse -> listener.onSuccess(listTResponse.getData()), listener::onFailed);
    }

    public void videotype(onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).videotype(), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void video(int typeId, int PageNum, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).video(PageNum, 16, typeId, ShareUtils.getUserInfo() != null ? ShareUtils.getUserInfo().getUserId() : 0), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void typehot(onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).typehot(), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }


    public void news(onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).news(), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void homecategory(int typeid, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).homecategory(typeid), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void paperquery(int pageNum, int pageSize, String typeName, onResponseListener<List<PaperTypeBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).paperquery(pageNum, pageSize, typeName), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void download(int paperId, onResponseListener<String> listener) {
        if (ShareUtils.getUserInfo() == null) {
            return;
        }
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).download(paperId, ShareUtils.getUserInfo().getUserId()), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void collect(int paperId, int videoId, onResponseListener listener) {
        if (ShareUtils.getUserInfo() == null) {
            return;
        }
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).collect(paperId, ShareUtils.getUserInfo().getUserId(), videoId), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void deletecollect(int paperId, int videoId, onResponseListener listener) {
        if (ShareUtils.getUserInfo() == null) {
            return;
        }
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).deletecollect(paperId, ShareUtils.getUserInfo().getUserId(), videoId), homePageBeanTResponse -> listener.onSuccess(homePageBeanTResponse.getData()), listener::onFailed);
    }

    public void Upload(List<MultipartBody.Part> partLis, onResponseListener<String> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).Upload(partLis), stringTResponse -> listener.onSuccess(stringTResponse.getData()), listener::onFailed);
    }

    public void collectlist(int pageNum, int pageSize, onResponseListener<List<PaperTypeBean>> listener) {
        if (ShareUtils.getUserInfo() == null) {
            return;
        }
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).collectlist(pageNum, pageSize, ShareUtils.getUserInfo().getUserId()), listTResponse -> listener.onSuccess(listTResponse.getData()), throwable -> listener.onFailed(throwable));
    }


    public void downloadlist(int pageNum, int pageSize, onResponseListener<List<PaperTypeBean>> listener) {
        if (ShareUtils.getUserInfo() == null) {
            return;
        }
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).downloadlist(pageNum, pageSize, ShareUtils.getUserInfo().getUserId()), listTResponse -> listener.onSuccess(listTResponse.getData()), throwable -> listener.onFailed(throwable));
    }

    public void sharelist(int pageNum, int pageSize, onResponseListener<List<PaperTypeBean>> listener) {
        if (ShareUtils.getUserInfo() == null) {
            return;
        }
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).sharelist(pageNum, pageSize, ShareUtils.getUserInfo().getUserId()), listTResponse -> listener.onSuccess(listTResponse.getData()), throwable -> listener.onFailed(throwable));
    }

    public void browselist(int pageNum, int pageSize, onResponseListener<List<PaperTypeBean>> listener) {
        if (ShareUtils.getUserInfo() == null) {
            return;
        }
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).browselist(pageNum, pageSize, ShareUtils.getUserInfo().getUserId()), listTResponse -> listener.onSuccess(listTResponse.getData()), throwable -> listener.onFailed(throwable));
    }

    public void share(int paperId, int videoId) {
        if (ShareUtils.getUserInfo() == null) {
            return;
        }
        HashMap<String, Integer> map = new HashMap<>();
        if (paperId != 0) {
            map.put("paperId", paperId);
        }
        if (videoId != 0) {
            map.put("paperId", videoId);
        }
        map.put("userId", ShareUtils.getUserInfo().getUserId());
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).share(map), new Consumer<TResponse>() {
            @Override
            public void accept(TResponse tResponse) throws Exception {

            }
        });
    }

    public void weChat(String wechatId, String wechatName, String wechatUrl, onResponseListener<TResponse<UserInfoBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).weChat(wechatId, wechatName, wechatUrl), listener::onSuccess, listener::onFailed);
    }

    public void weChatCode(String phone, onResponseListener<String> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).weChatCode(phone), stringTResponse -> listener.onSuccess(stringTResponse.getData()), listener::onFailed);
    }

    public void weChatLogin(String password, String phoneNumber, String sendCode, String wechatId, onResponseListener<TResponse<UserInfoBean>> listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).weChatLogin(password, phoneNumber, sendCode, wechatId), listener::onSuccess, listener::onFailed);
    }

    public void deletebrowse(String paperId, String VideoId, onResponseListener listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).deletebrowse(paperId, ShareUtils.getUserInfo() == null ? 0 : ShareUtils.getUserInfo().getUserId(), VideoId), (Consumer<TResponse>) listener::onSuccess, listener::onFailed);
    }

    public void deleteshare(String paperId, String VideoId, onResponseListener listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).deleteshare(paperId, ShareUtils.getUserInfo() == null ? 0 : ShareUtils.getUserInfo().getUserId(), VideoId), (Consumer<TResponse>) listener::onSuccess, listener::onFailed);
    }

    public void deletecollects(String paperId, String VideoId, onResponseListener listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).deletecollects(paperId, ShareUtils.getUserInfo() == null ? 0 : ShareUtils.getUserInfo().getUserId(), VideoId), (Consumer<TResponse>) listener::onSuccess, listener::onFailed);
    }

    public void deletedownload(String paperId, String VideoId, onResponseListener listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).deletedownload(paperId, ShareUtils.getUserInfo() == null ? 0 : ShareUtils.getUserInfo().getUserId(), VideoId), (Consumer<TResponse>) listener::onSuccess, listener::onFailed);
    }
}

