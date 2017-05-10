package com.ifnoif.androidtestdemo.intent_test;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shen on 17/3/1.
 */

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("service", "onStartJob");
        startService(new Intent(getApplicationContext(), PushService.class));

        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("service", "onStopJob");
        return false;
    }


    @Override
    public void onDestroy() {
        Log.d("service", "onDestroy");
        super.onDestroy();
    }
}
