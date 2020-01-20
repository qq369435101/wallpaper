package com.company.wallpaper.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;



import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-11-19.
 */

public class ListHotBean extends BaseObservable {
    @Bindable
  private   List<PaperTypeBean> list;

    public List<PaperTypeBean> getList() {
        return list;
    }

    public void setList(List<PaperTypeBean> list) {
        this.list = list;
        notifyPropertyChanged(com.company.wallpaper.BR.list);
    }

    public ListHotBean(List<PaperTypeBean> list) {
        this.list = list;
    }
}
