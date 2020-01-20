package com.company.wallpaper.bean;

import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-11-27.
 */

public class PaperTypeBean {

    /**
     * searchValue : null
     * createBy : null
     * createTime : null
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * typeId : 1
     * typeName : 热门搜索
     * typeCover : http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/1574064123411ChMkJ1auzYaIf_vRACZA_c2TT2IAAH9GAFa1u0AJkEV356.jpg
     * flag : false
     */

    private int typeId;
    private String typeName;
    private String typeCover;
    private String typeRepresent;
    private String videoName;
    private String videoUrl;
    private String videoRepresent;
    private String paperUrl;
    private int paperId;
    private int videoId;
    private String creatTime;
    private boolean flg;
    private boolean isAd=false;

    public boolean isAd() {
        return isAd;
    }

    public void setIsAd(boolean isAd) {
        this.isAd = isAd;
    }

    private List<paperListBean> paperList;


    public List<paperListBean> getPaperList() {
        return paperList;
    }

    public void setPaperList(List<paperListBean> paperList) {
        this.paperList = paperList;
    }


    public boolean isFlg() {
        return flg;
    }

    public void setFlg(boolean flg) {
        this.flg = flg;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

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

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoRepresent() {
        return videoRepresent;
    }

    public void setVideoRepresent(String videoRepresent) {
        this.videoRepresent = videoRepresent;
    }

    public String getPaperUrl() {
        return paperUrl;
    }

    public void setPaperUrl(String paperUrl) {
        this.paperUrl = paperUrl;
    }

    public String getTypeRepresent() {
        return typeRepresent;
    }

    public void setTypeRepresent(String typeRepresent) {
        this.typeRepresent = typeRepresent;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCover() {
        return typeCover;
    }

    public void setTypeCover(String typeCover) {
        this.typeCover = typeCover;
    }
}
