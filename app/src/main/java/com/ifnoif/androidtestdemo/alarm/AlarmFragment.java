package com.ifnoif.androidtestdemo.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.MainActivity;
import com.ifnoif.androidtestdemo.R;
import com.miui.powerkeeper.IPowerKeeper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shen on 17/3/6.
 */

public class AlarmFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.start_alarm)
    public void onStartAlarm(View v) {
        startAlarmExact(getContext());
        startExactAndAllowWhileIdle(getContext());
//        startAlarmClock(getContext());

        Toast.makeText(getContext(), "已启动Alarm", Toast.LENGTH_SHORT).show();
    }

    public static void startAlarmExact(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.syh.action.alarm");
        intent.putExtra("type", 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60000, pendingIntent);
    }


    public static void startExactAndAllowWhileIdle(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.syh.action.idlealarm");
        intent.putExtra("type", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60000, pendingIntent);
        }

    }

    public static void startAlarmClock(final Context context) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent("com.syh.action.alarmclock");
                intent.putExtra("type", 2);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent showIntent = new Intent(context, MainActivity.class);
                PendingIntent showPendingIntent = PendingIntent.getActivity(context, 101, showIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 60000, showPendingIntent), pendingIntent);
            }
        }.start();

    }


    @OnClick(R.id.start_service)
    public void onStartService(View view) {
        Intent intent = new Intent(getContext(), ForegroundService.class);
        getContext().startService(intent);
    }

    @OnClick(R.id.test_huawei)
    public void startTestHuawei(View view) {
        ifHuaweiAlert();
    }

    private void ifHuaweiAlert() {
        final SharedPreferences settings = getContext().getSharedPreferences("ProtectedApps", MODE_PRIVATE);
        final String saveIfSkip = "skipProtectedAppsMessage";
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isCallable(intent)) {
                final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(getContext());
                dontShowAgain.setText("Do not show again");
                dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor.putBoolean(saveIfSkip, isChecked);
                        editor.apply();
                    }
                });

                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Huawei Protected Apps")
                        .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", getString(R.string.app_name)))
                        .setView(dontShowAgain)
                        .setPositiveButton("Protected Apps", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                huaweiProtectedApps();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                editor.putBoolean(saveIfSkip, true);
                editor.apply();
            }
        }
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getContext().getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void huaweiProtectedApps() {
        if (true) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            startActivityForResult(intent, 199);
            return;
        }
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                String userSerial = getUserSerial();
                cmd += " --user " + userSerial;
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private String getUserSerial() {
        //noinspection ResourceType
        Object userManager = getContext().getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 199) {
            Log.d(TAG, "onActivityResult from huawei");
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                for (String key : keys) {
                    Log.d(TAG, "onActivityResult key:" + key + " value:" + bundle.get(key));
                }
            }
        }
    }

    @OnClick(R.id.query_xiaomi)
    public void queryXiaomi() {
        if (myService == null) {
            bindXiaoMi();
            return;
        } else {

            Bundle localBundle = new Bundle();
            localBundle.putString("userConfigureStatus", getContext().getPackageName());
            localBundle.putString("App", getContext().getPackageName());
            localBundle.putString("AppConfigure", "no_restrict");//miui_auto,restrict_bg,no_bg
            localBundle.putInt("UserId",getContext().getApplicationInfo().uid);

            try {
                int result = myService.setPowerSaveAppConfigure(localBundle);
                Log.d(TAG, "setPowerSaveAppConfigure result:" + result);


                Bundle newLocalBundle = new Bundle();
                newLocalBundle.putString("App", getContext().getPackageName());
                newLocalBundle.putInt("UserId",getContext().getApplicationInfo().uid);
                Bundle resultBundle = new Bundle();
                try {
                    int code = this.myService.getPowerSaveAppConfigure(newLocalBundle, resultBundle);
                    if (code == 0) {
                        String res = resultBundle.getString("AppConfigure");
                        Log.d(TAG, "getPowerSaveAppConfigure result:" + res );
                    } else {
                        Log.d(TAG, "getPowerSaveAppConfigure code:" + code);
                    }
                } catch (RemoteException paramString) {

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "setPowerSaveAppConfigure e:" + e.toString());
            }
            return;
        }


    }

    private void bindXiaomiDB() {
        //以下不可行，没有权限
//        new Thread() {
//            @Override
//            public void run() {
//                Uri uri = Uri.parse("content://com.miui.powerkeeper.configure/userTable");
//                Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
//                if (cursor != null && cursor.moveToFirst()) {
//                    Log.d(TAG, "query success");
//                    String[] columns = cursor.getColumnNames();
//                    for (String column : columns) {
//                        Log.d(TAG, "column:" + column);
//                    }
//                    return;
//                }
//
//                Log.d(TAG, "query fail");
//            }
//        }.start();
    }

    public void bindXiaoMi() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.PowerKeeperBackgroundService"));

        getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    IPowerKeeper myService;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = IPowerKeeper.Stub.asInterface(service);
            Log.d(TAG, "syh onServiceConnectedname:" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "syh onServiceDisconnected name:" + name);
        }
    };

    @OnClick(R.id.query_huawei)
    public void queryHuawei(View view){
        new Thread() {
            @Override
            public void run() {
                Uri uri = Uri.parse("content://com.huawei.android.smartpowerprovider");
                Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    Log.d(TAG, "query success");
                    String[] columns = cursor.getColumnNames();
                    for (String column : columns) {
                        Log.d(TAG, "column:" + column);
                    }
                    return;
                }

                Log.d(TAG, "query fail");
            }
        }.start();
    }
}
