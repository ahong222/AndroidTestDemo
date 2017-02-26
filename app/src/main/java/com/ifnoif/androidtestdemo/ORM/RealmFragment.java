package com.ifnoif.androidtestdemo.ORM;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by apple on 17-2-15.
 */

public class RealmFragment extends BaseFragment {
    private static final String TAG = "RealmFragment";
    private static int index = 0;

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.add)
    View mAddView;

    List<DBInfo> mDataList = new ArrayList<DBInfo>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.realm_fragment, container, false);
        ButterKnife.bind(this, view);

        init();
        return view;
    }

    private void init() {
        initRealm(getContext());

        List<DBInfo> result = queryAll();
        if (result != null) {
            mDataList.addAll(result);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mAdapter);

        mAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBInfo dbInfo = new DBInfo();
                dbInfo.id = UUID.randomUUID().toString();
                dbInfo.name = "item " + index;
                index++;


                mDataList.add(dbInfo);
                mAdapter.notifyItemInserted(mDataList.size() - 1);
                addDBInfoToDB(dbInfo);
            }
        });


    }

    private RecyclerView.Adapter<ViewHolder> mAdapter = new RecyclerView.Adapter<ViewHolder>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.db_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    };

    private class ViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            delete = (Button) itemView.findViewById(R.id.delete);
        }

        public void onBind(int position) {
            DBInfo dbInfo = mDataList.get(position);
            name.setText(dbInfo.name);

            delete.setTag(position);
            delete.setOnClickListener(mDeleteListener);
        }
    }

    private View.OnClickListener mDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Object tag = view.getTag();
            if (tag instanceof Integer) {
                int position = (Integer) tag;

                DBInfo dbInfo = mDataList.get(position);
                deleteDBInfoFromDB(dbInfo);

                mDataList.remove(position);
                mAdapter.notifyDataSetChanged();

            }
        }
    };

    private void deleteDBInfoFromDB(DBInfo dbInfo) {
        Realm realm = Realm.getDefaultInstance();

        // All changes to data must happen in a transaction

        final DBInfo managedDBInfo = realm.where(DBInfo.class).equalTo("name", dbInfo.name).findFirst();
        if (managedDBInfo != null) {

        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                managedDBInfo.deleteFromRealm();
            }
        });

    }

    private void addDBInfoToDB(DBInfo dbInfo) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        DBInfo info = realm.createObject(DBInfo.class);
        info.id = dbInfo.id;
        info.name = dbInfo.name;
        realm.commitTransaction();
    }

    private List<DBInfo> queryAll() {
        RealmResults<DBInfo> results = Realm.getDefaultInstance().where(DBInfo.class).findAll();
        return results;
    }

    public void initRealm(Context context) {
//        byte[] key = new byte[64];
//        new SecureRandom().nextBytes(key);
        Realm.init(context);
//        RealmMigration migration = new RealmMigration() {
//            @Override
//            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
//                Log.d(TAG, "migrate oldVersion:" + oldVersion + " newVersion:" + newVersion);
//            }
//        };
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("realmdb.realm") //文件名
                .schemaVersion(1) //版本号
//                .migration(migration)//数据库版本迁移（数据库升级，当数据库中某个表添加字段或者删除字段）
                .deleteRealmIfMigrationNeeded()//声明版本冲突时自动删除原数据库(当调用了该方法时，上面的方法将失效)。
                .build();//创建
        Realm.setDefaultConfiguration(config);
    }

    public void destroyRealm() {
        Realm.getDefaultInstance().close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyRealm();
    }
}
