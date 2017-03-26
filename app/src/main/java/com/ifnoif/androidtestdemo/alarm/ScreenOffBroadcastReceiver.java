package com.ifnoif.androidtestdemo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shen on 17/3/23.
 */

public class ScreenOffBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "syh";
    private String action = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            Log.d(TAG, "ACTION_SCREEN_ON");
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            Log.d(TAG, "ACTION_SCREEN_OFF");
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            Log.d(TAG, "ACTION_USER_PRESENT");
        }
    }

}
