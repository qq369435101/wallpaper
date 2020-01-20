package com.company.wallpaper.network;

import android.arch.lifecycle.ViewModel;

/**
 * Created by yushengyang.
 * Date: 2019/1/23.
 */

public abstract class BaseViewModel<T extends BaseRepository> extends ViewModel {
    public T repository;

    public BaseViewModel() {
        initRepository();
    }

    public abstract void initRepository();

    @Override
    protected void onCleared() {
        if (repository != null) {
            repository.onDestroy();
        }
        super.onCleared();
    }
}
