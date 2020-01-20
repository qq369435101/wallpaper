package com.company.wallpaper.bean;

/**
 * Created by yushengyang.
 * Date: 2019-11-26.
 */

public class paperListBean {

    /**
     * searchValue : null
     * createBy : null
     * createTime : null
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * paperId : 22
     * flg : false
     * paperName : 迪丽热巴
     * paperUrl : http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/1574069008204f9f189e973cf91fe7400ceee1828b867.jpeg
     * userName : admin
     * typeId : null
     * typeName : null
     * typeIds : null
     */


    private String paperUrl;
    private int paperId;
    private int videoId;

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public String getPaperUrl() {
        return paperUrl;
    }

    public void setPaperUrl(String paperUrl) {
        this.paperUrl = paperUrl;
    }
}
