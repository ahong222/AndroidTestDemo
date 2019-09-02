package com.ifnoif.androidtestdemo.hook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;

import com.ifnoif.androidtestdemo.BaseActivity;

public class RealActivity1 extends BaseActivity {
    private static final String TAG = "RealActivity1";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        Button button= new Button(this);
        button.setText("this is realActivity");
        setContentView(button);
    }
}
