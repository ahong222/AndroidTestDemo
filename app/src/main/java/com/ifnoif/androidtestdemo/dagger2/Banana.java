package com.ifnoif.androidtestdemo.dagger2;

import javax.inject.Inject;

/**
 * @Description:
 * @Author: shen
 * @Date: 2019-09-21
 */
public class Banana {
    private String name="";
    @Inject
    public Banana() {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
