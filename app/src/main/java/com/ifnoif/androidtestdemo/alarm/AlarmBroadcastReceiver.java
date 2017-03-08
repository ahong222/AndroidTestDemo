package com.ifnoif.androidtestdemo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by shen on 17/3/6.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static int count = 0;
    private static int sIdleCount = 0;
    private static int sExactCount = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager network = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = network.getActiveNetworkInfo();
        boolean connect = (networkInfo == null ? false : (networkInfo.isConnected()));

        int type = intent.getIntExtra("type", -1);
        if (type == 0) {
            AlarmFragment.startAlarmExact(context);
            sExactCount++;
            Log.d("AlarmBroadcastReceiver", "onReceive exact connect:" + connect + " sExactCount:" + sExactCount);
        } else if (type == 1) {
            AlarmFragment.startExactAndAllowWhileIdle(context);
            sIdleCount++;
            Log.d("AlarmBroadcastReceiver", "onReceive idle connect:" + connect + " sIdleCount:" + sIdleCount);
        } else if (type == 2) {
            count++;
            AlarmFragment.startAlarmClock(context);
            Log.d("AlarmBroadcastReceiver", "onReceive alarm clock,connect:" + connect + " count:" + count);
        }

    }
}
