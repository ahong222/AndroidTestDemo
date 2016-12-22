package com.ifnoif.androidtestdemo.share_transation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by syh on 2016/9/21.
 */
public class MyProgressBar extends ProgressBar {
    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void clearAnimation() {
        Log.d("syh", "clearAnimation");
        Thread.dumpStack();
        super.clearAnimation();
    }
}
