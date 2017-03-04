package com.ifnoif.androidtestdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ifnoif.androidtestdemo.ORM.RealmFragment;
import com.ifnoif.androidtestdemo.customview.CustomViewFragment;
import com.ifnoif.androidtestdemo.intent_test.IntentFragment;
import com.ifnoif.androidtestdemo.scroller.WheelFragment;
import com.ifnoif.androidtestdemo.share_transation.MainShare;
import com.ifnoif.androidtestdemo.touch.TouchFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ArrayList<ItemData> mTestList = new ArrayList<ItemData>();
    private RecyclerView.Adapter<MyViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListData();
        initListView();

        addSystemView();
    }

    private void initListData() {
        mTestList.add(new ItemData("Touch事件分发", TouchFragment.class));
        mTestList.add(new ItemData("Collapse动画", CollapseToolbarFragment.class));
        mTestList.add(new ItemData("Scroller用法", WheelFragment.class));
        mTestList.add(new ItemData("自定义View", CustomViewFragment.class));
        mTestList.add(new ItemData("共享动画", MainShare.class));
        mTestList.add(new ItemData("插件加载", LoadClassDemo.class));
        mTestList.add(new ItemData("Realm数据库", RealmFragment.class));
        mTestList.add(new ItemData("Volley", VolleyFragment.class));
        mTestList.add(new ItemData("Intent Test", IntentFragment.class));
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

    public void addSystemView() {
        Button button = new Button(this);
        button.setText("demo");
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams(50, 50);
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;

        wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        /*
        * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
        */
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        wmParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL; // 调整悬浮窗口至右侧中间
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        windowManager.addView(button, wmParams);
    }
}
