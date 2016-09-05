package com.ifnoif.androidtestdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ifnoif.androidtestdemo.customview.CustomViewFragment;
import com.ifnoif.androidtestdemo.scroller.WheelFragment;
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
    }

    private void initListData() {
        mTestList.add(new ItemData("Touch事件分发", TouchFragment.class));
        mTestList.add(new ItemData("Collapse动画", CollapseToolbarFragment.class));
        mTestList.add(new ItemData("Scroller用法", WheelFragment.class));
        mTestList.add(new ItemData("自定义View", CustomViewFragment.class));
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
        public Class<? extends Fragment> classArg;

        public ItemData(String title, Class<? extends Fragment> classArg) {
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
            try {
                Fragment fragment = itemData.classArg.newInstance();
                showFragment(fragment);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }
    }
}
