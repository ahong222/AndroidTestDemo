package com.ifnoif.androidtestdemo.dagger2;

import javax.inject.Inject;

/**
 * @Description:
 * @Author: shen
 * @Date: 2019-09-21
 */
public class FruitBox {
    Apple apple;
    Banana banana;

    @Inject
    public FruitBox(Apple apple, Banana banana) {
        this.apple = apple;
        this.banana = banana;
    }

    public String getName() {
        return apple.getName() + "_" + banana.getName();
    }
}

