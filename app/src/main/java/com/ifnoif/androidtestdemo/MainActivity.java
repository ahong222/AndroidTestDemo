package com.ifnoif.androidtestdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ifnoif.androidtestdemo.ORM.RealmActivity;
import com.ifnoif.androidtestdemo.access_bility.AccessBilityFragment;
import com.ifnoif.androidtestdemo.account.AccountMainActivity;
import com.ifnoif.androidtestdemo.alarm.AlarmFragment;
import com.ifnoif.androidtestdemo.customview.CustomViewFragment;
import com.ifnoif.androidtestdemo.intent_test.IntentFragment;
import com.ifnoif.androidtestdemo.intent_test.PushService;
import com.ifnoif.androidtestdemo.kotlin.KotlinActivity;
import com.ifnoif.androidtestdemo.okhttp.OkHttpFragment;
import com.ifnoif.androidtestdemo.scroller.WheelFragment;
import com.ifnoif.androidtestdemo.share_transation.MainShare;
import com.ifnoif.androidtestdemo.touch.TouchFragment;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    private ArrayList<ItemData> mTestList = new ArrayList<ItemData>();
    private RecyclerView.Adapter<MyViewHolder> mAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListData();
        initListView();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 100);

        Hello.main();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initListData() {
        mTestList.add(new ItemData("Touch事件分发", TouchFragment.class));
        mTestList.add(new ItemData("Collapse动画", CollapseToolbarFragment.class));
        mTestList.add(new ItemData("Scroller用法", WheelFragment.class));
        mTestList.add(new ItemData("自定义View", CustomViewFragment.class));
        mTestList.add(new ItemData("共享动画", MainShare.class));
        mTestList.add(new ItemData("插件加载", LoadClassDemo.class));
        mTestList.add(new ItemData("Realm数据库", RealmActivity.class));

        mTestList.add(new ItemData("Volley", VolleyFragment.class));
        mTestList.add(new ItemData("Intent Test", IntentFragment.class));
        mTestList.add(new ItemData("Alarm Test", AlarmFragment.class));
        mTestList.add(new ItemData("账户测试", AccountMainActivity.class));
        mTestList.add(new ItemData("OkHttp", OkHttpFragment.class));
        mTestList.add(new ItemData("模拟点击", AccessBilityFragment.class));
        mTestList.add(new ItemData("Kotlin测试", KotlinActivity.class));


    }

    private void initListView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecyclerView.Adapter<MyViewHolder>() {
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_layout, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
                holder.onBindViewHolder(mTestList.get(position));
            }

            @Override
            public int getItemCount() {
                return mTestList.size();
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.show(fragment);

        fragmentTransaction.commit();
        try {
            getSupportFragmentManager().executePendingTransactions();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Fragment getCurrentFragment() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList == null) {
            return null;
        }
        for (Fragment fragment : fragmentList) {
            if (fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private static class ItemData {
        public String title;
        public Class classArg;

        public ItemData(String title, Class classArg) {
            this.title = title;
            this.classArg = classArg;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemView.setOnClickListener(this);
        }

        public void onBindViewHolder(ItemData itemData) {
            mTitle.setText(itemData.title);
            this.itemView.setTag(itemData);
        }

        @Override
        public void onClick(View view) {
            ItemData itemData = (ItemData) view.getTag();
            if (Fragment.class.isAssignableFrom(itemData.classArg)) {
                try {
                    Fragment fragment = (Fragment) itemData.classArg.newInstance();
                    showFragment(fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            } else {
                startActivity(new Intent(MainActivity.this, itemData.classArg));
            }


        }
    }

    //以下会输出两遍 "Hello, world"
    public static class Hello {

        Runnable r1 = () -> {
            System.out.println(this);
        };
        Runnable r2 = () -> {
            System.out.println(toString());
        };

        public String toString() {
            return "Hello, world";
        }

        public static void main(String... args) {
            new Hello().r1.run();
            new Hello().r2.run();
        }

        public String getName() {
            return "";
        }

        public static void sort(Hello[] list) {
            Comparator<Hello> byName = new Comparator<Hello>() {
                @Override
                public int compare(Hello lhs, Hello rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            };

            Comparator<Hello> byName2 = (Hello lhs, Hello rhs) -> lhs.getName().compareTo(rhs.getName());
            Comparator<Hello> byName3 = (Hello lhs, Hello rhs) -> lhs.getName().compareTo(rhs.getName());

//        Comparator<Hello> byName4 = Comparator.comp(Hello::getName);

            Comparator<Hello> byName4 = (Hello x, Hello y) -> {
                return x.getName().compareTo(y.getName());
            };

            List<Hello> list1 = null;
            Collections.sort(list1, byName4);

//            byName4 = Comparator.comparing((Hello p) -> p.getName());
            Collections.sort(list1, byName4);

            //Comparator.compare(p -> p.getName());
            Arrays.sort(list, byName2);
        }

        interface Iterator<E> {
            boolean hasNext();

            E next();

            void remove();

//            default void skip(int i) {
//                for (; i > 0 && hasNext(); i -= 1) next();
//            }
        }

    }


}
