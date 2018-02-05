package com.ifnoif.androidtestdemo.access_bility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by shen on 17/3/27.
 */

public class AccessOtherAppService extends AccessibilityService {
    private static final String TAG = "AccessOtherAppService";
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
        Log.d(TAG, "AccessibilityEvent event:" + event);

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        AccessibilityNodeInfo result = findNodeInfo(rootNode, "神隐模式");
        Log.d(TAG, "result:" + result);
        if (result != null) {
            result.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private AccessibilityNodeInfo findNodeInfo(AccessibilityNodeInfo rootNode, String text) {
        AccessibilityNodeInfo nodeInfo;
        Log.d(TAG, "findNodeInfo count:" + rootNode.getChildCount());
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            nodeInfo = rootNode.getChild(i);
            Log.d(TAG, "findNodeInfo node:" + nodeInfo);
            if (nodeInfo.getText() != null && nodeInfo.getText().toString().contains(text)) {
                return nodeInfo;
            }

            nodeInfo = findNodeInfo(nodeInfo, text);
            if (nodeInfo != null) {
                return nodeInfo;
            }
        }
        return null;
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }


}
