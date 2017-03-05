package com.ifnoif.androidtestdemo.ORM;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.ifnoif.androidtestdemo.MainActivity;

import java.util.IllegalFormatException;

import io.realm.Realm;

/**
 * Created by shen on 17/3/4.
 */

public class RealmTestService extends Service {

    public RealmTestService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {
            @Override
            public void run() {

                onHandleIntent(null);
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void onHandleIntent(Intent intent) {
        Log.d("syh", "onHandleIntent");
        RealmActivity.initRealm(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        updateFirstItemCount(realm);
    }

    public static void updateFirstItemCount(Realm realm) {
        for (int i = 0; i < 1000; i++) {
            realm.beginTransaction();
            final DBInfo managedDBInfo = realm.where(DBInfo.class).equalTo("name", "first").findFirst();
            if (managedDBInfo != null) {
                managedDBInfo.count++;
                realm.commitTransaction();
                Log.d("syh", "count:" + managedDBInfo.count + " thread:" + Thread.currentThread().getId());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("syh", "no data, thread:" + Thread.currentThread().getId());
                return;
            }

        }
    }
}
