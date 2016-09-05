package com.ifnoif.androidtestdemo.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by syh on 2016/9/3.
 */
public class MyViewGroup extends LinearLayout {
    private final String TAG = "MyViewGroup";
    Scroller mScroller = null;
    private boolean s1 = true;

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        Log.d(TAG, "computeScroll");
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    public void beginScroll() {
        if (!s1) {
            mScroller.startScroll(0, 0, 0, 0, 1000);
            s1 = true;
        } else {
            mScroller.startScroll(0, 0, -500, 0, 1000);
            s1 = false;
        }
        invalidate();
    }
}
