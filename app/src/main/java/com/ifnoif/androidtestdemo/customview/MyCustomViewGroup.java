package com.ifnoif.androidtestdemo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by syh on 2016/9/3.
 */
public class MyCustomViewGroup extends ViewGroup {
    private int childWidth = 0;

    public MyCustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            int width = MeasureSpec.getSize(widthMeasureSpec);
            childWidth = width / childCount;
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).measure(childWidth, heightMeasureSpec);
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        LinearLayout linearLayout;
        int left = 0;
        int top = 0;
        int width = childWidth;
        int height = getHeight();
        for (int i = 0; i < getChildCount(); i++) {
            left = childWidth * i;
            getChildAt(i).layout(l + left, t + top, l + left + width, t + top + height);
        }
    }
}
