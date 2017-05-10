package com.ifnoif.androidtestdemo.jobservice_crash;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.ifnoif.androidtestdemo.R;


public class MyActivity extends Activity {

    public static final String TAG = MyActivity.class.getSimpleName();

    private static final int SCHEDULING_DELAY = 1;

    private static boolean sAutoReSchedulingEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobservce);
    }

    private static Handler sDelayHandler = new Handler();

    private void delayedScheduleJob() {
        if (sAutoReSchedulingEnabled) {
            sDelayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyTaskService.scheduleJob(MyActivity.this);
//                    delayedScheduleJob();
                }
            }, SCHEDULING_DELAY);
        }
    }

    public void onStartScheduler(View view) {
        Log.i(TAG, "onStartScheduler");
        sAutoReSchedulingEnabled = true;
        delayedScheduleJob();
    }

    public void onStopScheduler(View view) {
        Log.i(TAG, "onStopScheduler");
        sAutoReSchedulingEnabled = false;
    }
}
