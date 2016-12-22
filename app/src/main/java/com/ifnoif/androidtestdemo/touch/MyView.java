package com.ifnoif.androidtestdemo.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ifnoif.androidtestdemo.MyLog;

/**
 * Created by syh on 2016/9/1.
 */
public class MyView extends View {
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MyLog.d("onTouch:" + MyViewGroup.getTouch(event) + " tag:" + getTag()+"====");
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        MyLog.d("dispatchTouchEvent:" + MyViewGroup.getTouch(ev) + " tag:" + getTag()+"====");
        boolean result = super.dispatchTouchEvent(ev);
        MyLog.d("dispatchTouchEvent:" + MyViewGroup.getTouch(ev) + " tag:" + getTag()+"===="+" result:"+result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        if(getTag().equals("view2") && ev.getAction()==MotionEvent.ACTION_MOVE){
//            return false;
//        }
        MyLog.d("onTouchEvent:" + MyViewGroup.getTouch(ev) + " tag:" + getTag()+"====");
        return true;//super.onTouchEvent(ev);
    }
}
