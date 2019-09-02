package com.ifnoif.androidtestdemo.rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;

public class MergeUtils {

    public static void testMerge() {
        Observable<String> observable = Observable.merge(Observable.just("a"),Observable.just("b"));
        observable.subscribe(new Subject<String>() {
            @Override
            public boolean hasObservers() {
                return false;
            }

            @Override
            public boolean hasThrowable() {
                return false;
            }

            @Override
            public boolean hasComplete() {
                return false;
            }

            @Override
            public Throwable getThrowable() {
                return null;
            }

            @Override
            protected void subscribeActual(Observer<? super String> observer) {

            }

            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext s:"+s);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
