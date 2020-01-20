package com.company.wallpaper.bean;

import com.bytedance.sdk.openadsdk.TTFeedAd;

import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-11-26.
 */

public class HomePageBean {

    /**
     * searchValue : null
     * createBy : null
     * createTime : null
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * typeId : 37
     * typeName : 特色锁屏
     * typeCover : http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/1574064123411ChMkJ1auzYaIf_vRACZA_c2TT2IAAH9GAFa1u0AJkEV356.jpg
     * typeRepresent : 超级好用的壁纸a
     * paperList : [{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"paperId":9,"flg":false,"paperName":"美女","paperUrl":"http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/1574064123411ChMkJ1auzYaIf_vRACZA_c2TT2IAAH9GAFa1u0AJkEV356.jpg","userName":"admin","typeId":null,"typeName":null,"typeIds":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"paperId":10,"flg":false,"paperName":"女神","paperUrl":"http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/1574064123411ChMkJ1auzYaIf_vRACZA_c2TT2IAAH9GAFa1u0AJkEV356.jpg","userName":"admin","typeId":null,"typeName":null,"typeIds":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"paperId":11,"flg":false,"paperName":"杨幂","paperUrl":"http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/1574064123411ChMkJ1auzYaIf_vRACZA_c2TT2IAAH9GAFa1u0AJkEV356.jpg","userName":"admin","typeId":null,"typeName":null,"typeIds":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"paperId":12,"flg":false,"paperName":"1","paperUrl":"http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/157406865178657a2eab8149cb856d6eb935579403589.jpg","userName":"admin","typeId":null,"typeName":null,"typeIds":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"paperId":13,"flg":false,"paperName":"2","paperUrl":"http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/1574068666188063ad5bdcbb51578f6be8130c7a04c8a.jpg","userName":"admin","typeId":null,"typeName":null,"typeIds":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"paperId":14,"flg":false,"paperName":"3","paperUrl":"http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/15740686779034f8f4eaaecdfca71f101671999c838d2.jpg","userName":"admin","typeId":null,"typeName":null,"typeIds":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"paperId":15,"flg":false,"paperName":"4","paperUrl":"http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/1574068804530ef91b0fb4464d61c657fa107dbb28d0d.jpeg","userName":"admin","typeId":null,"typeName":null,"typeIds":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"paperId":11,"flg":false,"paperName":"杨幂","paperUrl":"http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/1574064123411ChMkJ1auzYaIf_vRACZA_c2TT2IAAH9GAFa1u0AJkEV356.jpg","userName":"admin","typeId":null,"typeName":null,"typeIds":null},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"paperId":12,"flg":false,"paperName":"1","paperUrl":"http://tiger-oss.oss-cn-beijing.aliyuncs.com/tiger-oss/avatar/157406865178657a2eab8149cb856d6eb935579403589.jpg","userName":"admin","typeId":null,"typeName":null,"typeIds":null}]
     * typeIds : null
     * typeHot : 1
     * flag : false
     */

//    private ParamsBean params;
    private int typeId;
    private String typeName;
    private String typeCover;
    private String typeRepresent;
    //    private Object typeIds;
//    private int typeHot;
//    private boolean flag;
    private List<paperListBean> paperList;

    private TTFeedAd ad;

    public TTFeedAd getAd() {
        return ad;
    }

    public void setAd(TTFeedAd ad) {
        this.ad = ad;
    }

    public static class ParamsBean {
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


    public List<paperListBean> getPaperList() {
        return paperList;
    }

    public void setPaperList(List<paperListBean> paperList) {
        this.paperList = paperList;
    }

    public String getTypeRepresent() {
        return typeRepresent;
    }

    public void setTypeRepresent(String typeRepresent) {
        this.typeRepresent = typeRepresent;
    }
}

