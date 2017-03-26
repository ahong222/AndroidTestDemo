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

import com.ifnoif.androidtestdemo.ORM.RealmActivity;
import com.ifnoif.androidtestdemo.account.AccountMainActivity;
import com.ifnoif.androidtestdemo.alarm.AlarmFragment;
import com.ifnoif.androidtestdemo.customview.CustomViewFragment;
import com.ifnoif.androidtestdemo.intent_test.IntentFragment;
import com.ifnoif.androidtestdemo.intent_test.PushService;
import com.ifnoif.androidtestdemo.okhttp.OkHttpFragment;
import com.ifnoif.androidtestdemo.scroller.WheelFragment;
import com.ifnoif.androidtestdemo.share_transation.MainShare;
import com.ifnoif.androidtestdemo.touch.TouchFragment;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    private ArrayList<ItemData> mTestList = new ArrayList<ItemData>();
    private RecyclerView.Adapter<MyViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListData();
        initListView();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 100);
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

}
