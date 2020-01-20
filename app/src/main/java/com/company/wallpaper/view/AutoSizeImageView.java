package com.company.wallpaper.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ysy on 2016/9/21.
 */
public class AutoSizeImageView extends ImageView {

    public AutoSizeImageView(Context context) {
        this(context, null);
    }

    public AutoSizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            int modeW = MeasureSpec.getMode(widthMeasureSpec);
            int modeH = MeasureSpec.getMode(heightMeasureSpec);
            int sizeW = MeasureSpec.getSize(widthMeasureSpec);
            int sizeH = MeasureSpec.getSize(heightMeasureSpec);
            if (modeW == MeasureSpec.EXACTLY) {
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                sizeH = sizeW * height / width;
                setMeasuredDimension(sizeW, sizeW);
            }
        }
    }
}
