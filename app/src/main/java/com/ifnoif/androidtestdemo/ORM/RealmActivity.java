package com.ifnoif.androidtestdemo.ORM;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ifnoif.androidtestdemo.BaseActivity;
import com.ifnoif.androidtestdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by apple on 17-2-15.
 */

public class RealmActivity extends BaseActivity {
    private static final String TAG = "RealmActivity";
    private static int index = 0;

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.add)
    View mAddView;

    List<DBInfo> mDataList = new ArrayList<DBInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_fragment);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mAdapter);

        mAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBInfo dbInfo = new DBInfo();
                dbInfo.id = UUID.randomUUID().toString();
                dbInfo.name = mDataList.size() == 0 ? "first" : "item " + index;
                index++;


                mDataList.add(dbInfo);
                mAdapter.notifyItemInserted(mDataList.size() - 1);
                addDBInfoToDB(dbInfo);
            }
        });


    }

    private void initData() {
        new AsyncTask<Void, Void, List<DBInfo>>() {
            @Override
            protected List<DBInfo> doInBackground(Void... params) {
                long time = System.currentTimeMillis();
                initRealm(RealmActivity.this);
                Log.d(TAG, "init db time:" + (System.currentTimeMillis() - time));

                List<DBInfo> result = queryAll();
                Log.d(TAG, "queryAll time:" + (System.currentTimeMillis() - time));

                return Realm.getDefaultInstance().copyFromRealm(result);
            }

            @Override
            protected void onPostExecute(List<DBInfo> result) {
                if (result != null) {
                    mDataList.clear();
                    mDataList.addAll(result);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
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
        TextView count;
        Button update;
        Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            count = (TextView) itemView.findViewById(R.id.count);
            update = (Button) itemView.findViewById(R.id.update);
            delete = (Button) itemView.findViewById(R.id.delete);
        }

        public void onBind(int position) {
            DBInfo dbInfo = mDataList.get(position);
            name.setText(dbInfo.v1);
            count.setText(dbInfo.count + "");

            update.setTag(position);
            update.setOnClickListener(mUpdateListener);
            delete.setTag(position);
            delete.setOnClickListener(mDeleteListener);
        }
    }

    private View.OnClickListener mUpdateListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag instanceof Integer) {
                int position = (Integer) tag;

                final DBInfo dbInfo = mDataList.get(position);
                dbInfo.count++;
                dbInfo.v1 = "v"+((int)(100*Math.random()));
                mAdapter.notifyItemChanged(position);

                Realm realm = Realm.getDefaultInstance();

                // All changes to data must happen in a transaction

//                realm.beginTransaction();
//                final DBInfo managedDBInfo = realm.where(DBInfo.class).equalTo("name", dbInfo.name).findFirst();
//                if (managedDBInfo != null) {
//                    managedDBInfo.count = dbInfo.count;
//                    managedDBInfo.v1 = dbInfo.v1;
//                }
//                realm.commitTransaction();

                realm.executeTransactionAsync(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm) {
                        final DBInfo managedDBInfo = realm.where(DBInfo.class).equalTo("name", dbInfo.name).findFirst();
                        if (managedDBInfo != null) {
                            managedDBInfo.count = dbInfo.count;
                            managedDBInfo.v1 = dbInfo.v1;
                        }
                    }
                }, new Realm.Transaction.OnSuccess(){
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"update onSuccess");
                    }
                },new Realm.Transaction.OnError(){
                    @Override
                    public void onError(Throwable error) {
                        Log.d(TAG,"update onError");
                    }
                });
            }
        }
    };
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
        long time = System.currentTimeMillis();

        Realm realm = Realm.getDefaultInstance();

        // All changes to data must happen in a transaction

        realm.beginTransaction();
        final DBInfo managedDBInfo = realm.where(DBInfo.class).equalTo("name", dbInfo.name).findFirst();
        if (managedDBInfo != null) {
            managedDBInfo.deleteFromRealm();
        }
        realm.commitTransaction();

        Log.d(TAG, "delete time:" + (System.currentTimeMillis() - time));
    }

    private void addDBInfoToDB(DBInfo dbInfo) {
        long time = System.currentTimeMillis();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        DBInfo info = realm.createObject(DBInfo.class);
        info.id = dbInfo.id;
        info.name = dbInfo.name;
        realm.commitTransaction();

        Log.d(TAG, "add time:" + (System.currentTimeMillis() - time));
    }

    private List<DBInfo> queryAll() {
        RealmResults<DBInfo> results = Realm.getDefaultInstance().where(DBInfo.class).findAll();
        return results;
    }

    public static void initRealm(Context context) {
//        byte[] key = new byte[64];
//        new SecureRandom().nextBytes(key);
        Realm.init(context);
        //如果没有修改字段，可以升级版本
        MyMigration migration = new MyMigration(6) {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                Log.d(TAG, "migrate oldVersion:" + oldVersion + " newVersion:" + newVersion);

                if (oldVersion == 1 && newVersion >= 2) {
                    realm.getSchema().get(DBInfo.class.getSimpleName()).addField("v1", String.class);
                }
                //如果修改了字段，修改了版本号，那么升级上来的app会出现无法保存数据的情况
                if (oldVersion <3 && newVersion >= 3) {
                    realm.getSchema().get(DBInfo.class.getSimpleName()).addField("v2", String.class);
                }

                if (oldVersion <5 && newVersion >= 5) {
                    realm.getSchema().get(DBInfo.class.getSimpleName()).addField("v3", String.class);
                }
            }

            @Override
            public boolean equals(Object o) {

                return super.equals(o);
            }
        };
        /**
         * 版本记录
         * 3:增加了column_v3
         */
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("realmdb.realm") //文件名
                .schemaVersion(migration.getVersion()) //版本号
                .migration(migration)//数据库版本迁移（数据库升级，当数据库中某个表添加字段或者删除字段）
//                .deleteRealmIfMigrationNeeded()//声明版本冲突时自动删除原数据库(当调用了该方法时，上面的方法将失效)。
                .build();//创建

        Realm.setDefaultConfiguration(config);
        try{
            Realm.getDefaultInstance();
        }catch (RealmMigrationNeededException e){
            Realm.deleteRealm(config);//增加字段，没有升级版本号，或者升级版本号了没有修改字段
            e.printStackTrace();
        }catch (Exception e) {
            Realm.deleteRealm(config);
            e.printStackTrace();//从高版本降级抛出的异常
        }

    }

    public void destroyRealm() {
        Realm.getDefaultInstance().close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyRealm();
    }

    @OnClick(R.id.another_process)
    public void onClickAnotherProcess(View view) {
        Intent intent = new Intent(this, RealmTestService.class);
        startService(intent);

        new Thread() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                RealmTestService.updateFirstItemCount(realm);

            }
        }.start();
    }

    public static class MyMigration implements RealmMigration {
        private int version = 0;
        public MyMigration(int ver){
            this.version = ver;
        }
        public int getVersion(){
            return version;
        }
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof MyMigration){
                Log.d(TAG,"equals v:"+((MyMigration) o).getVersion()+" this:"+version);
                return version==((MyMigration) o).getVersion();
            }
            return false;
        }
    }
}
