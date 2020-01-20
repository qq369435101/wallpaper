package com.company.wallpaper.network.service;


import com.company.wallpaper.bean.PaperTypeBean;
import com.company.wallpaper.bean.UserInfoBean;
import com.company.wallpaper.repository.Response_Wechat;
import com.company.wallpaper.repository.Response_WechatUserInfo;
import com.ysy.commonlib.base.TResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 *
 */

public interface HttpService {

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url);

    @GET("paperlist")
    Observable<TResponse<List<PaperTypeBean>>> paperlist(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    @GET("papertype")
    Observable<TResponse<List<PaperTypeBean>>> papertype();

    @GET("papertypesun")
    Observable<TResponse<List<PaperTypeBean>>> papertypesun(@Query("typeId") int typeId);

    @GET("typecategory")
    Observable<TResponse<List<PaperTypeBean>>> typecategory(@Query("typeId") int typeId);

    @GET("sendCode")
    Observable<TResponse<String>> sendCode(@Query("phone") String phone);

    @GET("register")
    Observable<TResponse> register(@Query("loginName") String loginName, @Query("password") String password, @Query("passwordb") String passwordb, @Query("sendCode") String sendCode);

    @GET("suncategory")
    Observable<TResponse<List<PaperTypeBean>>> suncategory(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("typeId") int typeId, @Query("typeName") String typeName);

    @GET("checkcollect")
    Observable<TResponse<List<PaperTypeBean>>> checkcollect(@Query("paperId") int paperId, @Query("typeId") int typeId, @Query("userId") int userId, @Query("videoId") int videoId);

    @GET("checkcollect")
    Observable<TResponse<List<PaperTypeBean>>> checkcollect(@Query("paperId") int paperId, @Query("typeName") String typeName, @Query("userId") int userId, @Query("videoId") int videoId, @Query("typeId") int typeId);

    @GET("checkcollect")
    Observable<TResponse<List<PaperTypeBean>>> checkcollect(@Query("fromType") String fromType, @Query("paperId") int paperId, @Query("userId") int userId, @Query("videoId") int videoId, @Query("typeId") int typeId);

    @GET("login")
    Observable<TResponse<UserInfoBean>> login(@QueryMap HashMap<String, String> map);

    @GET("loginSms")
    Observable<TResponse<UserInfoBean>> loginSms(@QueryMap HashMap<String, String> map);


    @GET("loginCode")
    Observable<TResponse<String>> loginCode(@Query("phone") String phone);

    @GET("version")
    Observable<TResponse> version();


    @GET("videotype")
    Observable<TResponse<List<PaperTypeBean>>> videotype();

    @GET("video")
    Observable<TResponse<List<PaperTypeBean>>> video(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("typeId") int typeId, @Query("userId") int userId);

    @GET("checkcollect")
    Observable<TResponse<List<PaperTypeBean>>> checkcollect(@Query("typeId") int typeId, @Query("userId") int userId, @Query("videoId") int videoId);

    @GET("typehot")
    Observable<TResponse<List<PaperTypeBean>>> typehot();

    @GET("news")
    Observable<TResponse<List<PaperTypeBean>>> news();


    @GET("homecategory")
    Observable<TResponse<List<PaperTypeBean>>> homecategory(@Query("typeId") int typeId);

    @GET("paperquery")
    Observable<TResponse<List<PaperTypeBean>>> paperquery(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("typeName") String typeName);


    @GET("download")
    Observable<TResponse<String>> download(@Query("paperId") int paperId, @Query("userId") int userId);

    @GET("collect")
    Observable<TResponse> collect(@Query("paperId") int paperId, @Query("userId") int userId, @Query("videoId") int videoId);

    @GET("deletecollect")
    Observable<TResponse> deletecollect(@Query("paperId") int paperId, @Query("userId") int userId, @Query("videoId") int videoId);

    /**
     * 通过表单上传图片
     *
     * @param partLis
     * @return
     */
    @Multipart
    @POST("avator")
    //请求方法为POST，里面为你要上传的url
    Observable<TResponse<String>> Upload(@Part List<MultipartBody.Part> partLis);


    /**
     * 获取微信登录userInfo
     *
     * @param params
     * @return
     */
    @GET("https://api.weixin.qq.com/sns/userinfo")
    Observable<Response_WechatUserInfo> WeChatUserInfo(@QueryMap Map<String, String> params);


    /**
     * 微信登录
     *
     * @param map
     * @return
     */
    @POST("user/WeChat")
    Observable<TResponse<com.tencent.bugly.crashreport.biz.UserInfoBean>> WeChatLogin(@Body() HashMap<String, String> map);

    /**
     * 获取微信登录token
     *
     * @param params
     * @return
     */
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<Response_Wechat> WeChatToken(@QueryMap Map<String, String> params);

    @GET("collectlist")
    Observable<TResponse<List<PaperTypeBean>>> collectlist(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("userId") int userId);

    @GET("downloadlist")
    Observable<TResponse<List<PaperTypeBean>>> downloadlist(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("userId") int userId);

    @GET("sharelist")
    Observable<TResponse<List<PaperTypeBean>>> sharelist(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("userId") int userId);

    @GET("browselist")
    Observable<TResponse<List<PaperTypeBean>>> browselist(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("userId") int userId);

    @GET("typevideo")
    Observable<TResponse<List<PaperTypeBean>>> typevideo();

    @GET("share")
    Observable<TResponse> share(@QueryMap HashMap<String, Integer> map);

    @GET("weChat")
    Observable<TResponse<UserInfoBean>> weChat(@Query("wechatId") String wechatId, @Query("wechatName") String wechatName, @Query("wechatUrl") String wechatUrl);


    @GET("weChatCode")
    Observable<TResponse<String>> weChatCode(@Query("phone") String phone);

    @GET("weChatLogin")
    Observable<TResponse<UserInfoBean>> weChatLogin(@Query("password") String password, @Query("phoneNumber") String phoneNumber, @Query("sendCode") String sendCode, @Query("wechatId") String wechatId);

    @GET("deletebrowse")
    Observable<TResponse> deletebrowse(@Query("paperId") String paperId, @Query("userId") int userId, @Query("videoId") String videoId);

    @GET("deletedownload")
    Observable<TResponse> deletedownload(@Query("paperId") String paperId, @Query("userId") int userId, @Query("videoId") String videoId);

    @GET("deleteshare")
    Observable<TResponse> deleteshare(@Query("paperId") String paperId, @Query("userId") int userId, @Query("videoId") String videoId);

    @GET("deletecollects")
    Observable<TResponse> deletecollects(@Query("paperId") String paperId, @Query("userId") int userId, @Query("videoId") String videoId);


}