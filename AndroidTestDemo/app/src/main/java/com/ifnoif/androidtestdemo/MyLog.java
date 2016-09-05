package com.ifnoif.androidtestdemo;

import android.util.Log;

/**
 * Created by syh on 2016/9/1.
 */
public class MyLog {
    public static final String TAG = "MyLog";

    public static void d(String log) {
        Log.d(TAG, log);
    }

    public static void e(String log) {
        Log.e(TAG, log);
    }
}
