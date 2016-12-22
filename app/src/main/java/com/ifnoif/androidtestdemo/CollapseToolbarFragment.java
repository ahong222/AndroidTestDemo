package com.ifnoif.androidtestdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by syh on 2016/9/2.
 */
public class CollapseToolbarFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.collaps_toolbar_layout, container, false);
//        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.MyToolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("My Toolbar Tittle");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
