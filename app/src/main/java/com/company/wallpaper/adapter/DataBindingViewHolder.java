package com.company.wallpaper.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by yushengyang.
 * Date: 2018/8/23.
 */

public class DataBindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private T bindview;

    public DataBindingViewHolder(@NonNull T  bindview) {
        super(bindview.getRoot());
        this.bindview = bindview;
        mViews = new SparseArray<>();
    }


    public T getBindview() {
        return bindview;
    }
}
