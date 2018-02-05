package com.ifnoif.androidtestdemo.anotations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;

/**
 * Created by shen on 17/9/29.
 */

public class AnotationFragment extends BaseFragment {

    @Override
    public int getContentResource() {
        return R.layout.anotation_fragment;
    }
}

