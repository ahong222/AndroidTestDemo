package com.ifnoif.androidtestdemo.dagger2;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @Description:
 * @Author: shen
 * @Date: 2019-09-21
 */
@MyScope
public class Apple {
    @Inject
    public Apple() {

    }

    public String name = "apple"+Math.random();
    public String getName() {
        return name;
    }
}
