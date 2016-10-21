package com.ifnoif.androidtestdemo;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by syh on 2016/10/8.
 */
public class LoadClassDemo extends BaseFragment implements View.OnClickListener {
    public static int hostValue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.load_class_layout, container, false);
        view.findViewById(R.id.load).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load:
                hostValue = 20;
                ClassLoader classLoader = initDexClassLoader();
                loadClass(classLoader,10);

                classLoader = initDexClassLoader();
                loadClass(classLoader, 200);

                classLoader = getClass().getClassLoader();
                loadClass(classLoader, 7000);
                break;
        }
    }

    public ClassLoader initDexClassLoader(){
        String apkPath = Environment.getExternalStorageDirectory().getPath() + "/app-debug.apk";
        String optimizedDirectory = getContext().getFilesDir().getAbsolutePath() + "/dex";
        File file = new File(optimizedDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath, optimizedDirectory, null, getClass().getClassLoader());
        return dexClassLoader;
    }

    public void loadClass(ClassLoader classLoader, int testValue){
        hostValue += testValue;
        try {
            Class pluginClass = classLoader.loadClass("com.ifnoif.pluginapplication.PluginEnter");

            Object pluginEnter = pluginClass.newInstance();

            Method method = pluginClass.getDeclaredMethod("getText");
            method.setAccessible(true);

            Object object = method.invoke(pluginEnter);
            Log.d("syh", "syh start value:" + object);

            Field valueField = pluginClass.getDeclaredField("value");
            valueField.setAccessible(true);
            valueField.set(pluginEnter, testValue);

            object = method.invoke(pluginEnter);
            Log.d("syh", "syh end value:" + object);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("syh", "syh loadClass err:" + e.toString());
            Toast.makeText(getActivity(), "null", Toast.LENGTH_LONG).show();
        }
    }
}
