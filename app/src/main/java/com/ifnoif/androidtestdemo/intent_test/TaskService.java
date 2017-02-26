package com.ifnoif.androidtestdemo.intent_test;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shen on 17/2/26.
 */

public class TaskService extends IntentService {
    public static final String TAG = TaskService.class.getSimpleName();

    public TaskService(){
        super("default");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TaskService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int index = intent.getIntExtra("index", -1);
        Log.d(TAG, "start index:" + index + " this:" + this.hashCode());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "end index:" + index + " this:" + this.hashCode());

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate this:" + this.hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy this:" + this.hashCode());
    }
}
