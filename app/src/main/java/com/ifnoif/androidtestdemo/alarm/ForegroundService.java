package com.ifnoif.androidtestdemo.alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.ifnoif.androidtestdemo.MainActivity;
import com.ifnoif.androidtestdemo.R;

/**
 * Created by shen on 17/3/6.
 */

public class ForegroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setDefaults(Notification.DEFAULT_LIGHTS)
//                .setLargeIcon(iconBitmap)
//                .setSmallIcon(R.drawable.ic_shop)
//                .setAutoCancel(true)
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle(title)
//                .setContentText(messgage)
//                .setContentIntent(PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT));
//        return builder;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder.setLargeIcon(null)
                .setWhen(0)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("前台服务运行中")
                .setContentIntent(PendingIntent.getActivity(this, 1000, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        startForeground(1982, notification);

    }
}
