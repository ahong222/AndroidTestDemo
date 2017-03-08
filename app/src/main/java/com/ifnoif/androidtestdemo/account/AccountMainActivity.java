package com.ifnoif.androidtestdemo.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ifnoif.androidtestdemo.R;

import java.util.Set;

public class AccountMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_main);
        try {
            Set<String> keys = getIntent().getExtras().keySet();
            for (String key : keys) {
                Log.d("syh", "onCreate key:" + key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加账号
                Account account = new Account("account_test", "com.mytype");
                AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                accountManager.addAccountExplicitly(account, null, null);

                //设置同步
                ContentResolver.setIsSyncable(account, "com.myprovider", 1);
                ContentResolver.setSyncAutomatically(account, "com.myprovider", true);
                ContentResolver.addPeriodicSync(account, "com.myprovider", new Bundle(), 10);
                //10秒是2分钟，3*60是可能是3分钟，也可能6分钟，时间不固定
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("syh", "onResume");
    }
}
