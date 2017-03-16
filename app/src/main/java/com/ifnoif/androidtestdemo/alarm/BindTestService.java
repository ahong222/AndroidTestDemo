package com.ifnoif.androidtestdemo.alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ifnoif.androidtestdemo.MainActivity;
import com.ifnoif.androidtestdemo.R;

/**
 * Created by shen on 17/3/15.
 */

public class BindTestService extends Service {
    public static final String TAG = "BindTestService";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        setForeground();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnBind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }


    public void setForeground(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder.setLargeIcon(null)
                .setWhen(0)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("前台服务运行中")
                .setContentIntent(PendingIntent.getActivity(this, 1000, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        startForeground(1982, notification);
    }

    public static class MyBinder extends Binder {

    }
}
