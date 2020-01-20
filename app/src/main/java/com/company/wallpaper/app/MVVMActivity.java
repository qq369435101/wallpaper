package com.company.wallpaper.app;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;

/**
 * Created by yushengyang.
 * Date: 2018/11/24.
 */

public abstract class MVVMActivity<SV extends ViewDataBinding,VM extends ViewModel> extends SwipeBackActivity<SV>  {
    protected VM mViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel=ViewModelProviders.of(this).get((Class<VM>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
    }

    public VM getViewModel(){
        return mViewModel;
    }
}
