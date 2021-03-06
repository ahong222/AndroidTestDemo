package com.ifnoif.androidtestdemo.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.ifnoif.androidtestdemo.MyLog;

/**
 * Created by syh on 2016/9/1.
 */
public class MyViewGroup extends LinearLayout {
    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static String getTouch(MotionEvent event) {
        String action = null;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                action = "down";
                break;
            case MotionEvent.ACTION_MOVE:
                action = "move";
                break;
            case MotionEvent.ACTION_POINTER_UP:
                action = "pointer_up";
                break;
            case MotionEvent.ACTION_UP:
                action = "up";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "up";
                break;
            default:
                action = event.getAction() + "";
                break;
        }
        return "Action:" + action+" x:"+event.getRawX()+" y:"+event.getRawY()+" hash:"+ event.hashCode()+" index:"+event.getActionIndex()+" pointCount:"+event.getPointerCount();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MyLog.d("dispatchTouchEvent:" + getTouch(ev));
        return super.dispatchTouchEvent(ev);
//        return true;//
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        MyLog.d("onInterceptTouchEvent:" + getTouch(ev));
        return super.onInterceptTouchEvent(ev);
//        return true;//
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        MyLog.d("onTouchEvent:" + getTouch(ev));
        return super.onTouchEvent(ev);
    }
}
