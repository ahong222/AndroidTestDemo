package com.ifnoif.androidtestdemo.dagger2;

public interface ITestContract {
    public interface ITestView {
        public void setPresenter(ITestPresenter presenter);
        public void setTitle(String title);
    }

    public interface ITestPresenter {
        public void requestTitle();
    }
}
