package com.company.wallpaper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

public class FlowRadioGroup extends RadioGroup {
    public FlowRadioGroup(Context context) {
        super(context);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int x = 0;
        int y = 0;
        int row = 0;
        for (int index = 0; index < childCount; index++) {
            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE) {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED); // 此处增加onlayout中的换行判断，用于计算所需的高度
                int marginEnd = ((LayoutParams) child.getLayoutParams()).getMarginEnd();
                int marginTop = ((LayoutParams) child.getLayoutParams()).topMargin;
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight() + marginTop;
                x += width;
                y = row * height + height + marginTop;
                if (x > maxWidth - 2 * marginEnd) {
                    x = width;
                    row++;
                    y = row * (height) + height;
                }
            }
        } // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y);
    }


    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View view = getChildAt(i);
            if (view != null && view.getVisibility() != GONE) {
                measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, 0);
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int maxWidth = r - l;
        int x = 0;
        int y = 0;
        int row = 0;
        int marginEnds = 0;
        boolean first = true;
        for (int i = 0; i < childCount; i++) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int marginEnd = ((RadioGroup.LayoutParams) child.getLayoutParams()).getMarginEnd();
                int marginTop = ((LayoutParams) child.getLayoutParams()).topMargin;
                if (i != 0) {
                    marginEnds += marginEnd;
                }
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width;
                y = row * (height + marginTop) + height;
                if (x + marginEnds > maxWidth) {
                    x = width;
                    row++;
                    y = row * (height + marginTop) + height;
                    marginEnds = 0;
                    first = true;
                }
                if (first) {
                    child.layout(x - width, y - height, x, y);
                    first = false;
                } else {
                    child.layout(x - width + marginEnds, y - height, x + marginEnds, y);
                }
            }
        }
    }
}
