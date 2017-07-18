package com.ifnoif.androidtestdemo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
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
import com.ifnoif.androidtestdemo.ORM.MyRealmLog;
import com.ifnoif.androidtestdemo.ORM.RealmActivity;
import com.ifnoif.androidtestdemo.access_bility.AccessBilityFragment;
import com.ifnoif.androidtestdemo.account.AccountMainActivity;
import com.ifnoif.androidtestdemo.alarm.AlarmFragment;
import com.ifnoif.androidtestdemo.customview.CustomViewFragment;
import com.ifnoif.androidtestdemo.intent_test.IntentFragment;
import com.ifnoif.androidtestdemo.jobservice_crash.MyActivity;
import com.ifnoif.androidtestdemo.kotlin.KotlinActivity;
import com.ifnoif.androidtestdemo.kotlin.KotlinFragment;
import com.ifnoif.androidtestdemo.music.MusicFragment;
import com.ifnoif.androidtestdemo.okhttp.OkHttpFragment;
import com.ifnoif.androidtestdemo.rxjava.RxFragment;
import com.ifnoif.androidtestdemo.scroller.WheelFragment;
import com.ifnoif.androidtestdemo.share_transation.MainShare;
import com.ifnoif.androidtestdemo.touch.TouchFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        mTestList.add(new ItemData("Kotlin Fragment", KotlinFragment.class));
        mTestList.add(new ItemData("JobService Crash", MyActivity.class));
        mTestList.add(new ItemData("Music Test", MusicFragment.class));
        mTestList.add(new ItemData("内存映射测试", MMapFragment.class));
        mTestList.add(new ItemData("RXJava", RxFragment.class));


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

            Intent intent = new Intent("android.intent.action.com.sankuai.meituan.meituanwaimaibusiness.remind_advance_order");
            sendBroadcast(intent);

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

}
