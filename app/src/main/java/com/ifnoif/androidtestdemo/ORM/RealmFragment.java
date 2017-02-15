package com.ifnoif.androidtestdemo.ORM;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by apple on 17-2-15.
 */

public class RealmFragment extends BaseFragment {

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
            name = (TextView)itemView.findViewById(R.id.name);
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
                mAdapter.notifyItemRemoved(position);

            }
        }
    };

    private void deleteDBInfoFromDB(DBInfo dbInfo) {

    }

    private void addDBInfoToDB(DBInfo dbInfo) {

    }

    public static class DBInfo {
        public String id;
        public String name;
    }
}
