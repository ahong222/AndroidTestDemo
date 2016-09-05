package com.ifnoif.androidtestdemo.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;

/**
 * Created by syh on 2016/9/3.
 */
public class CustomViewFragment extends BaseFragment {
    private MyCustomViewGroup myCustomViewGroup;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customview_layout, container, false);
        myCustomViewGroup = (MyCustomViewGroup)view.findViewById(R.id.myviewGroup);
        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomView myCustomView = new MyCustomView(getActivity(), Math.random()+"");
                myCustomViewGroup.addView(myCustomView);
            }
        });
        return view;
    }
}
