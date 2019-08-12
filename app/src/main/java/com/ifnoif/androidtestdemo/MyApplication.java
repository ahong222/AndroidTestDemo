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

    public MyApplication() {
        System.out.println("syh MyApplication");
        Log.i("syh","MyApplication");
        Log.d("syh","MyApplication d");
        Log.v("syh","MyApplication v");
    }

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

    public static void measureTime() {
        String methodName = null;
        String className = null;
        int line = 0;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length >= 4) {
            StackTraceElement element = elements[3];
            if (element != null) {
                methodName = element.getMethodName();
                className = element.getClassName();
                line = element.getLineNumber();
            }
        }
        String tag = className + "." + methodName + ":" + line;
        android.util.Log.d("syh", "syh tag:" + tag);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = getProcessName(this);
        Log.d("application", "onCreate hash:" + this.hashCode() + " processName:" + processName);
        sContext = this.getApplicationContext();

        if (getPackageName().equals(processName)) {
            Realm.init(getApplicationContext());
        }

        A.test("abc");
        A.test("cde");
        if (BuildConfig.DEBUG) {
            A.test("aaa");
        }

        measureTime();
//        startService(new Intent(this, PushService.class));
    }

    public static class A {
        public static void test(String a) {
            if (BuildConfig.DEBUG) {
                Log.d("syh", "a:" + a);
            }
        }
    }

}
