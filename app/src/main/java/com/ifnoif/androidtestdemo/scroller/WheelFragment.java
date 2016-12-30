package com.ifnoif.androidtestdemo.scroller;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.ifnoif.androidtestdemo.R;

/**
 * Created by syh on 2016/9/3.
 * 使用scroller辅助view scrollTo来做动画：
 * 主要在调用Scroller.startScroll后，postInvalidate，
 * 然后会走到View的computeScroll回调，此时判断如果scroller没有finish的话更新view的scroll，然后再刷新，以此循环
 */
public class WheelFragment extends Fragment {


    VelocityTracker mVelocityTracker;
    private MyViewGroup myViewGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scroller_layout, container, false);
        myViewGroup = (MyViewGroup) view.findViewById(R.id.myviewGroup);
        view.findViewById(R.id.scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewGroup.beginScroll();
            }
        });
        view.findViewById(R.id.fling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewGroup.beginFling();
            }
        });
        view.findViewById(R.id.scroll).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.addMovement(event);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int max = ViewConfiguration.get(v.getContext()).getScaledMaximumFlingVelocity();
                    int min = ViewConfiguration.get(v.getContext()).getScaledMinimumFlingVelocity();
                    mVelocityTracker.computeCurrentVelocity(1000, max);
                    Log.d("syh", "onTouch v:" + mVelocityTracker.getXVelocity() + " max:" + max+" min:"+min);
                }
                return true;
            }
        });
        return view;
    }


}
