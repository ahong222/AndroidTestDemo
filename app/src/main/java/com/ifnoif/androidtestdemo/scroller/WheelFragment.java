package com.ifnoif.androidtestdemo.scroller;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifnoif.androidtestdemo.R;

/**
 * Created by syh on 2016/9/3.
 * 使用scroller辅助view scrollTo来做动画：
 * 主要在调用Scroller.startScroll后，postInvalidate，
 * 然后会走到View的computeScroll回调，此时判断如果scroller没有finish的话更新view的scroll，然后再刷新，以此循环
 *
 */
public class WheelFragment extends Fragment {


    private MyViewGroup myViewGroup;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scroller_layout,container, false);
        myViewGroup = (MyViewGroup) view.findViewById(R.id.myviewGroup);
        view.findViewById(R.id.scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll();
            }
        });
        return view;
    }

    private void scroll() {

        myViewGroup.beginScroll();

    }

}
