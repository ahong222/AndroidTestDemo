package com.ifnoif.androidtestdemo.okhttp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;

/**
 * Created by shen on 17/3/22.
 */

public class OkHttpFragment extends BaseFragment {
    @BindView(R.id.url)
    EditText mUrl;

    @BindView(R.id.result)
    EditText mResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.okhttp_fragment, container, false);
        ButterKnife.bind(this, view);

        mUrl.setText("https://api.weibo.com/2/statuses/public_timeline.json");
        return view;
    }

    @OnClick(R.id.go)
    public void onRequestTest(View view) {
        String url = mUrl.getText().toString();
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(params[0])
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                mResult.setText(result);
            }
        }.execute(url);


    }
}
