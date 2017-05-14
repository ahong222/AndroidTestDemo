package com.ifnoif.androidtestdemo.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shen on 17/3/6.
 */

public class AlarmFragment extends BaseFragment {

    private static AlarmFragment instance;

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
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("syh", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("syh", "onServiceDisconnected");
        }
    };

    public static void startAlarmExact(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.syh.action.alarm");
        intent.putExtra("type", 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60000, pendingIntent);

        checkNetWork(context);
        wakeScreen(context);
    }

    public static void stopAlarmExact(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.syh.action.alarm");
        intent.putExtra("type", 10);
        intent.putExtra("newExtra", 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        instance = this;
        return view;
    }

    public void onStartAlarm(View v) {
        startAlarmExact(getActivity());
//        startExactAndAllowWhileIdle(getContext());
//        startAlarmClock(getContext());

        Toast.makeText(getContext(), "已启动Alarm", Toast.LENGTH_SHORT).show();
    }

    public void onStopAlarm(View v) {
        stopAlarmExact(getContext());
    }

    public void onStartService(View view) {
        Intent intent = new Intent(getContext(), ForegroundService.class);
        getContext().startService(intent);
    }

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

//    @OnClick(R.id.query_xiaomi)
    public void queryXiaomi() {
        if (myService == null) {
            bindXiaoMi();
            return;
        } else {

            Bundle localBundle = new Bundle();
            localBundle.putString("userConfigureStatus", getContext().getPackageName());
            localBundle.putString("App", getContext().getPackageName());
            localBundle.putString("AppConfigure", "no_restrict");//miui_auto,restrict_bg,no_bg
            localBundle.putInt("UserId", getContext().getApplicationInfo().uid);

            try {
                int result = myService.setPowerSaveAppConfigure(localBundle);
                Log.d(TAG, "setPowerSaveAppConfigure result:" + result);


                Bundle newLocalBundle = new Bundle();
                newLocalBundle.putString("App", getContext().getPackageName());
                newLocalBundle.putInt("UserId", getContext().getApplicationInfo().uid);
                Bundle resultBundle = new Bundle();
                try {
                    int code = this.myService.getPowerSaveAppConfigure(newLocalBundle, resultBundle);
                    if (code == 0) {
                        String res = resultBundle.getString("AppConfigure");
                        Log.d(TAG, "getPowerSaveAppConfigure result:" + res);
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

//    @OnClick(R.id.query_huawei)
    public void queryHuawei(View view) {
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

//    @OnClick(R.id.send_notification)
    public void onSendNotification(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        Notification notification = builder.setLargeIcon(null)
                .setWhen(0)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("测试通知")
                .setContentIntent(PendingIntent.getActivity(getContext(), 1000, new Intent(getContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        notification.sound = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.test_music);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(100, notification);

    }

//    @OnClick(R.id.start_bind_service)
    public void onStartBindService(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(getContext().getPackageName(), BindTestService.class.getName()));
        getContext().startService(intent);
    }

//    @OnClick(R.id.bind_service)
    public void onBindService(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(getContext().getPackageName(), BindTestService.class.getName()));
        getContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

//    @OnClick(R.id.unbind_service)
    public void onUnBindService(View view) {
        getContext().unbindService(mServiceConnection);
    }

//    @OnClick(R.id.show_alert_window)
    public void onShowAlertWindow(View view) {
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 100);
//            return;
//        }
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Button button = new Button(getContext());
        button.setText("test");
        button.setTextColor(Color.BLACK);
        button.setBackgroundColor(Color.RED);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager tmpWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                tmpWindowManager.removeView(v);
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(50, 30);
        layoutParams.width = 100;
        layoutParams.height = 60;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//

        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        windowManager.addView(button, layoutParams);
    }

    private static void checkNetWork(Context context) {
        boolean isConnected = isConnected(context);
        if (isConnected) {
            String url = "https://api.weibo.com/2/statuses/public_timeline.json";
            new AsyncTask<String, Void, String>() {

                @Override
                protected String doInBackground(String... params) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(params[0])
                            .build();

                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        return response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    Log.d("syh", "checkNetWork ok:" + (result != null));
                }
            }.execute(url);
        } else {
            Log.d("syh", "checkNetWork is not connected");
        }

    }

    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            boolean isConnected = networkinfo.isConnected();
            return isConnected;
        } catch (Exception e) {
            return false;
        }
    }


    private static PowerManager.WakeLock wakeLock;

    private static void wakeScreen(Context context) {
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "Tag");
        }
        if (!wakeLock.isHeld()) {
            Log.d("syh", "wakeScreen");
            wakeLock.acquire();
        } else {
            wakeLock.release();
            wakeLock.acquire();
            Log.d("syh", "wakeLock isHeld");
        }

//        if (instance != null) {
//            Log.d("syh", "add FLAG_KEEP_SCREEN_ON");
//            instance.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        }
    }


//    @OnClick(R.id.listen_screen_off)
    public void onListenScreenOff(View view) {
        BroadcastReceiver broadcastReceiver = new ScreenOffBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        getContext().registerReceiver(broadcastReceiver, intentFilter);
    }


}
