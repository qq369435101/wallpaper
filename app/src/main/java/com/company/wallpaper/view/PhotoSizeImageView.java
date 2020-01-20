package com.company.wallpaper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.company.wallpaper.R;

/**
 * Created by ysy on 2016/9/21.
 */
public class PhotoSizeImageView extends ImageView {
    private float radio;
    private float lineHeight;

    public PhotoSizeImageView(Context context) {
        this(context, null);
    }


    public PhotoSizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhotoSizeImageView);
        radio = typedArray.getFloat(R.styleable.PhotoSizeImageView_ratio, 1.0f);
        lineHeight = typedArray.getDimension(R.styleable.PhotoSizeImageView_line_height, 1.0f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        heightSize = (int) (widthSize * radio - lineHeight);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
//        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && radio != 0) {
//            heightSize = (int) (widthSize * radio - lineHeight);
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
//        } else if (widthMeasureSpec != MeasureSpec.EXACTLY && heightMeasureSpec == MeasureSpec.EXACTLY && radio != 0) {
//            heightSize = (int) (widthSize / radio - lineHeight);
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
//        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
