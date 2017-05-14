package com.ifnoif.androidtestdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.List;

import io.realm.Realm;

/**
 * Created by shen on 17/3/2.
 */

public class MyApplication extends Application {

    public static Context sContext;

    public static String getProcessName(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        if (processInfos == null) {
            return null;
        }
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid) {
                return info.processName;
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("application", "onCreate hash:" + this.hashCode());
        sContext = this.getApplicationContext();

        if (getPackageName().equals(getProcessName(this))) {
            Realm.init(getApplicationContext());
        }
//        startService(new Intent(this, PushService.class));
    }
}
