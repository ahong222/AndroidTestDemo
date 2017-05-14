package com.ifnoif.androidtestdemo.intent_test;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;

import java.util.List;

/**
 * Created by shen on 17/2/26.
 */

public class IntentFragment extends BaseFragment {
    public View startService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intent_test_layout, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        view.findViewById(R.id.job_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setJobService();
            }
        });
    }

    public void startTask(View view) {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    final int index = i;

                    Intent intent = new Intent(getActivity(), TaskService.class);
                    intent.putExtra("index", index);
                    getActivity().startService(intent);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();


    }

    public void setJobService() {
        Log.d(TAG, "setJobService");

        JobInfo.Builder builder = new JobInfo.Builder(100, new ComponentName(getContext(), MyJobService.class));
        builder.setPeriodic(10 * 1000);
        builder.setPersisted(true);
//        builder.setRequiresDeviceIdle(true);


        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}
