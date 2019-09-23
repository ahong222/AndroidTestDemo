package com.ifnoif.androidtestdemo.dagger2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.ifnoif.androidtestdemo.BaseActivity;
import com.ifnoif.androidtestdemo.R;

import javax.inject.Inject;

public class DaggerActivity extends BaseActivity implements ITestContract.ITestView {

//    @Inject
//    UserPresenter mUserPresenter;
    ITestContract.ITestPresenter mITestPresenter;

    @Inject
    Apple apple;

    @Inject
    Apple apple2;


    @Inject
    Banana banana;

    @Inject
    Banana banana2;

    @Inject
    FruitBox fruitBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger_layout);
        findViewById(R.id.update_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITestPresenter.requestTitle();
            }
        });
        mITestPresenter = new TestPresenter(this);


        Apple app = DaggerFruitComponent.builder().build().getApple();
        DaggerFruitComponent.builder().build().injectDaggerActivity(DaggerActivity.this);
        System.out.println("equals:"+(app.equals(this.apple)));
        System.out.println("apple equals:"+(apple == apple2));
        System.out.println("banana equals:"+ (banana==banana2));

        setTitle(apple.getName());
        setTitle(banana.getName());
        setTitle(fruitBox.getName());

    }

    @Override
    public void setPresenter(ITestContract.ITestPresenter presenter) {

    }

    @Override
    public void setTitle(final String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String oldTitle = ((TextView)findViewById(R.id.update_title)).getText().toString();
                ((TextView)findViewById(R.id.update_title)).setText(oldTitle+":"+title);
            }
        });
    }
}
