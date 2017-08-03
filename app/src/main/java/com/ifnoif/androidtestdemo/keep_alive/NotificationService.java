package com.ifnoif.androidtestdemo.keep_alive;

import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

/**
 * Created by shen on 17/8/2.
 */

@SuppressLint("OverrideAbstract")
public class NotificationService extends NotificationListenerService {

    @Override
    public void onListenerConnected() {
        System.out.println("zyf onListenerConnected");
        super.onListenerConnected();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        System.out.println("zyf onNotificationPosted");
        super.onNotificationPosted(sbn);
    }
}