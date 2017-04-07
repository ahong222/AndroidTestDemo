package com.ifnoif.androidtestdemo.access_bility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by shen on 17/3/27.
 */

public class AccessOtherAppService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("syh", "AccessibilityEvent event:" + event);
    }

    @Override
    public void onInterrupt() {
        Log.d("syh", "onInterrupt");
    }


}
