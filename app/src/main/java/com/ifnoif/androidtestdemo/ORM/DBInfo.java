package com.ifnoif.androidtestdemo.ORM;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by apple on 17-2-16.
 */

@RealmClass
public class DBInfo extends RealmObject {
    public String id;
    public String name;
    public int count = 0;

//    public void updateName() {
//        name = "random" + (int) (100 * Math.random());
//    }
//
//    public String getText() {
//        return name;
//    }

    /**
     * 版本
     * 1:add String v1；
     * 2:add String v2， remove v1
     *
     *
     */
    public String v1 = "v1";
    public String v2 = "v2";
    public String v3 = "v3";
    public String v6 = "v6";
}
