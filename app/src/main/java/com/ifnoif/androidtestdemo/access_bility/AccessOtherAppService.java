package com.ifnoif.androidtestdemo.access_bility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by shen on 17/3/27.
 */

public class AccessOtherAppService extends AccessibilityService {
//    @Override
//    protected void onServiceConnected() {
//        Log.d("syh", "onServiceConnected");
//        //动态配置
//        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
//        serviceInfo.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED|AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
//        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
//        serviceInfo.packageNames = new String[]{"com.android.settings","com.miui.powerkeeper"};
//        serviceInfo.notificationTimeout=100;
//        setServiceInfo(serviceInfo);
//    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("syh", "AccessibilityEvent event:" + event);

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
    }

    @Override
    public void onInterrupt() {
        Log.d("syh", "onInterrupt");
    }


}
