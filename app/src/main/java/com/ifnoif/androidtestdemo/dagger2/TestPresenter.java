package com.ifnoif.androidtestdemo.dagger2;

public class TestPresenter implements ITestContract.ITestPresenter {
    private ITestContract.ITestView mITestView;
    public TestPresenter(ITestContract.ITestView iTestView) {
        this.mITestView = iTestView;
    }
    @Override
    public void requestTitle() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mITestView.setTitle(Math.random()+"");
            }
        }.start();
    }
}
