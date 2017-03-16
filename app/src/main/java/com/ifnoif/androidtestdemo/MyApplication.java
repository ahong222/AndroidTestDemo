package com.ifnoif.androidtestdemo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.ifnoif.androidtestdemo.intent_test.PushService;

import io.realm.Realm;

/**
 * Created by shen on 17/3/2.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("application", "onCreate hash:" + this.hashCode());

        Realm.init(getApplicationContext());
//        startService(new Intent(this, PushService.class));
    }
}
