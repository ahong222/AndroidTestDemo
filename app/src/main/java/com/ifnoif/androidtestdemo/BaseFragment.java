package com.ifnoif.androidtestdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by syh on 2016/9/3.
 */
public class BaseFragment extends Fragment {
    public String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentResource(), container, false);
        init(view);
        return view;
    }

    public void init(View  view) {

    }


    public int getContentResource(){
        return 0;
    }
}
