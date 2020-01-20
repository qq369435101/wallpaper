package com.company.wallpaper.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

/**
 * Created by yushengyang.
 * Date: 2019-11-19.
 */

public class ListHistoryBean extends BaseObservable {
    @Bindable
  private   List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyPropertyChanged(BR.list);
    }

    public ListHistoryBean(List<String> list) {
        this.list = list;
    }
}
