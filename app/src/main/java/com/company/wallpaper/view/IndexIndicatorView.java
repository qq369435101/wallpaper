package com.company.wallpaper.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.company.wallpaper.R;


/**
 * Created by yushengyang.
 * Date: 2019/3/5.
 * 位置指示器
 */

public class IndexIndicatorView extends LinearLayout {

    public IndexIndicatorView(Context context) {
        super(context, null);

    }

    public IndexIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public IndexIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init(int mPageSize) {

        for (int i = 0; i < mPageSize; i++) {
            View inflate = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_indicator_unselect, null);
            addView(inflate);
        }
    }

}
