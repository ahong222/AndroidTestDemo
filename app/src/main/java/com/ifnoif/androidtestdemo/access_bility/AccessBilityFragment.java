package com.ifnoif.androidtestdemo.access_bility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;


/**
 * Created by shen on 17/3/27.
 */

public class AccessBilityFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.access_bility_fragment, container, false);
        return view;
    }

    public void showGuide(View v) {
        showBatteryGuide();
    }

    public void openBatterySettingActivity() {
        Intent intent = new Intent("android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS");
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$HighPowerApplicationsActivity"));
        startActivity(intent);
    }

    public void showBatteryGuide() {
        Context context = getContext();
        String packageName = context.getPackageName();
        Intent intent = new Intent();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(packageName);
//            if (!isIgnoringBatteryOptimizations) {
                try {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            }
        }
    }
}
