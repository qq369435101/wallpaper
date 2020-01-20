package com.company.wallpaper.repository;


import com.company.wallpaper.listener.onResponseListener;
import com.company.wallpaper.network.BaseRepository;
import com.company.wallpaper.network.RetrofitClient;
import com.company.wallpaper.network.service.HttpService;
import com.company.wallpaper.utils.wechatutils.WechatLoginHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yushengyang.
 * Date: 2018/10/16.
 */

public class WechatShareLoginRepository extends BaseRepository<Response_Wechat> {
    public void getUseInfo(Map<String, String> map, onResponseListener listener) {
        sendRequest(RetrofitClient.getInstance().getService(HttpService.class).WeChatToken(map), data -> {
                    WechatLoginHelper.openid = data.getOpenid();
                    HashMap<String, String> toaken_map = new HashMap<>();
                    toaken_map.put("access_token", data.getAccess_token());
                    toaken_map.put("openid", data.getOpenid());
                    toaken_map.put("lang", "zh_CN");
                    sendRequest(RetrofitClient.getInstance().getService(HttpService.class).WeChatUserInfo(toaken_map), userInfo -> {
                                listener.onSuccess("");
                                WechatLoginHelper.loginCallBack.onSuccess(userInfo);
                            }

                    );

                }

        );
    }
}
