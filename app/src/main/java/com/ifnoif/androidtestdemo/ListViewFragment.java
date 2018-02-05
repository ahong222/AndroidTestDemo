package com.ifnoif.androidtestdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shen on 17/10/15.
 */

public class ListViewFragment extends BaseFragment {
    private static final String TAG = "ListViewFragment";
    private ListView mListView;
    private BaseAdapter adapter;
    private ArrayList<String> data = new ArrayList<String>();

    @Override
    public int getContentResource() {
        return R.layout.listview_test;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtn1();
            }
        });

        view.findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtn2();
            }
        });

        view.findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtn3();
            }
        });

        mListView = (ListView) view.findViewById(R.id.list_view1);

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public String getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                }
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));
                return convertView;
            }
        };
        mListView.setAdapter(adapter);
    }

    private void onClickBtn1() {
        data.clear();
        for (int i = 0; i < 10; i++) {
            data.add("Type1_" + i);
        }
        adapter.notifyDataSetChanged();
    }

    private void onClickBtn2() {
        data.clear();
        for (int i = 0; i < 10; i++) {
            data.add("Type2_" + i);
        }
        adapter.notifyDataSetChanged();
    }

    private void onClickBtn3() {
        mListView.setVisibility(mListView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public static class MyListView extends ListView{
        public MyListView(Context context) {
            this(context, null);
        }
        public MyListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            Log.d(TAG,"onAttachedToWindow");
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            Log.d(TAG, "onDetachedFromWindow");
        }
    }
}
