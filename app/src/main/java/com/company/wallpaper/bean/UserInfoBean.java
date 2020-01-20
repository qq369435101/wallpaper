package com.company.wallpaper.bean;

/**
 * Created by yushengyang.
 * Date: 2019-11-28.
 */

public class UserInfoBean {


    /**
     * searchValue : null
     * createBy : null
     * createTime : null
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * userId : 123
     * loginName : 18210490506
     * password : 5544e0e720970ee19583c3174f99e847
     * salt : b97668
     * delFlag : 0
     * onlyId : 46D95162-BC4D-4B06-9AF1-FA9797908BF0
     * userAvator : null
     * systemversion : 13.1.2
     * appversion : 1.0
     * phonetype : iPhone 7 (CDMA)
     * admin : false
     */

    private int userId;
    private String loginName;
    private String password;
    private String salt;
    private String onlyId;
    private String userAvator;
    private String systemversion;
    private String appversion;
    private String phonetype;
    private boolean admin;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getOnlyId() {
        return onlyId;
    }

    public void setOnlyId(String onlyId) {
        this.onlyId = onlyId;
    }

    public String getUserAvator() {
        return userAvator;
    }

    public void setUserAvator(String userAvator) {
        this.userAvator = userAvator;
    }

    public String getSystemversion() {
        return systemversion;
    }

    public void setSystemversion(String systemversion) {
        this.systemversion = systemversion;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getPhonetype() {
        return phonetype;
    }

    public void setPhonetype(String phonetype) {
        this.phonetype = phonetype;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
