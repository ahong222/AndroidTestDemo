package com.ifnoif.androidtestdemo.hook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ifnoif.androidtestdemo.BaseActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class FakeActivity extends BaseActivity {
    private static final String TAG = "FakeActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }


    public static void initHook(Context context) {
        hookSystemHandler();
        try {
            Field defaultFiled = null;
            Object defaultValue = null;

            defaultFiled = Class.forName("android.app.ActivityManager").getDeclaredField("IActivityManagerSingleton");
            defaultFiled.setAccessible(true);
            defaultValue = defaultFiled.get(null);

            //反射SingleTon
            Class<?> SingletonClass = Class.forName("android.util.Singleton");
            Field mInstance = SingletonClass.getDeclaredField("mInstance");
            mInstance.setAccessible(true);
            //到这里已经拿到ActivityManager对象
            Object iActivityManagerObject = mInstance.get(defaultValue);


            //开始动态代理，用代理对象替换掉真实的ActivityManager，瞒天过海
            Class<?> IActivityManagerIntercept = Class.forName("android.app.IActivityManager");

            AmsInvocationHandler handler = new AmsInvocationHandler(context, iActivityManagerObject);

            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{IActivityManagerIntercept}, handler);

            //现在替换掉这个对象
            mInstance.set(defaultValue, proxy);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hookSystemHandler() {
        //用于将fakeActivity替换为RealActivity
        try {

            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            //获取主线程对象
            Object activityThread = currentActivityThreadMethod.invoke(null);
            //获取mH字段
            Field mH = activityThreadClass.getDeclaredField("mH");
            mH.setAccessible(true);
            //获取Handler
            Handler handler = (Handler) mH.get(activityThread);
            //获取原始的mCallBack字段
            Field mCallBack = Handler.class.getDeclaredField("mCallback");
            mCallBack.setAccessible(true);
            //这里设置了我们自己实现了接口的CallBack对象
            mCallBack.set(handler, new ActivityThreadHandlerCallback(handler)) ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ActivityThreadHandlerCallback implements Handler.Callback {

        private Handler handler;

        private ActivityThreadHandlerCallback(Handler handler) {
            this.handler = handler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            Log.i("HookAmsUtil", "handleMessage");
            //替换之前的Intent
            if (msg.what ==100) {
                Log.i("HookAmsUtil","lauchActivity");
                handleLauchActivity(msg);
            }

            handler.handleMessage(msg);
            return true;
        }

        private void handleLauchActivity(Message msg) {
            Object obj = msg.obj;//ActivityClientRecord
            try{
                Field intentField = obj.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                Intent proxyInent = (Intent) intentField.get(obj);
                Intent realIntent = proxyInent.getParcelableExtra("oldIntent");
                if (realIntent != null) {
                    Log.d(TAG, "替换成真实要启动的Activity："+realIntent);
                    proxyInent.setComponent(realIntent.getComponent());
                }
            }catch (Exception e){
                Log.i("HookAmsUtil","lauchActivity falied");
            }

        }
    }

    private static class AmsInvocationHandler implements InvocationHandler {

        private Object iActivityManagerObject;

        private Context context;
        private AmsInvocationHandler(Context context, Object iActivityManagerObject) {
            this.context =context;
            this.iActivityManagerObject = iActivityManagerObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.i("HookUtil", method.getName());
            //我要在这里搞点事情
            if ("startActivity".contains(method.getName())) {
                Log.e("HookUtil","Activity已经开始启动");
                Log.e("HookUtil","小弟到此一游！！！");

                Intent intent = null;
                int index = -1;
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof Intent) {
                        //说明找到了startActivity的Intent参数
                        intent = (Intent) args[i];
                        if (RealActivity1.class.getName().equals(intent.getComponent().getClassName())) {
                            //这个意图是不能被启动的，因为Acitivity没有在清单文件中注册
                            index = i;
                            break;
                        }
                    }
                }

                if (index >= 0) {
                    //伪造一个代理的Intent，代理Intent启动的是proxyActivity
                    Intent proxyIntent = new Intent();
                    ComponentName componentName = new ComponentName(context, FakeActivity.class);
                    proxyIntent.setComponent(componentName);
                    //保留真实要启动的Intent
                    proxyIntent.putExtra("oldIntent", intent);
                    args[index] = proxyIntent;
                }

            }
            Object result = method.invoke(iActivityManagerObject, args);
            if ("startActivity".contains(method.getName())) {
                Log.d(TAG, "startActivity hook result:"+result);
            }
            return result;
        }
    }
}
