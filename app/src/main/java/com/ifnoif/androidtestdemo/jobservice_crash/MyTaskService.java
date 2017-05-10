package com.ifnoif.androidtestdemo.jobservice_crash;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.Random;

public class MyTaskService extends JobService {

    public static final String TAG = MyTaskService.class.getSimpleName();

    private static int DELAY = 500;
    private static int OFFSET = 300;

    private static boolean sAutoReSchedulingEnabled = false;
    private static Random mRandom = new Random();

    public static void scheduleJob(Context context) {
        int jobId = 100 + mRandom.nextInt(10);
        Log.i(TAG, "scheduleJob: " + jobId);
        JobInfo myTask =
                new JobInfo.Builder(jobId, new ComponentName(context.getPackageName(), MyTaskService.class.getName()))
                        .setMinimumLatency(DELAY - OFFSET)
                        .setOverrideDeadline(DELAY + OFFSET)
                        .build();
        JobScheduler jobScheduler =
                (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myTask);
    }

    private static Handler sDelayHandler = new Handler();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "ON START JOB: " + params.getJobId());
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "ON STOP JOB: " + params.getJobId());
        return true;
    }

}
