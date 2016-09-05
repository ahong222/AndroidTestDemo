package com.ifnoif.androidtestdemo.touch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifnoif.androidtestdemo.R;

/**
 * Created by syh on 2016/9/1.
 * 问题：
 * 1.dispatchTouchEvent到底如何影响事件传递的
 *
 *
 * 测试:
 * .当parent dispatchTouchEvent返回true，但是不调用super.dispatchTouchEvent 时
 * parent 只能接收到dispatchTouchEvent down、up，child接收不到任何事件
 * .当parent dispatchTouchEvent返回true, 且调用super.dispatchTouchEvent 时
 * parent 能走完dispatchTouchEvent 和 onTouchEvent全过程，child都只能出发down事件
 * .当parent dispatchTouchEvent返回true, 且调用super.dispatchTouchEvent 时，且onInterceptTouchEvent返回true时
 * parent同上，child接收不到任何事件
 * .当parent dispatchTouchEvent返回true, 且调用super.dispatchTouchEvent 时，且onInterceptTouchEvent返回false,但不调用super..
 * 会走子view的onTouchEvent,super.onInterceptTouchEvent只是返回一个false
 *
 *
 *
 * 结论：
 * 1.分发顺序
 * group.dispatchTouchEvent
 * group.onInterceptTouchEvent
 * child.dispatchTouchEvent
 * child.onTouch
 * child.onTouchEvent
 * group.onTouchEvent
 * group.dispatchTouchEvent:move ...
 * 2.如何查找view
 * 在touchDown时，查找点击位置所处的view，加入链表，如多点触摸时，链表中会有多个target
 * 非touchDown或ACTION_POINTER_DOWN时，事件会追一调用target的dispatchTouchEvent,onTouchEvent
 *
 *
 *
 */
public class TouchFragment extends Fragment {

    public TouchFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.touch_layout, container, false);
        view.findViewById(R.id.view1).setTag("view1");
        view.findViewById(R.id.view2).setTag("view2");
        return view;
    }


}
