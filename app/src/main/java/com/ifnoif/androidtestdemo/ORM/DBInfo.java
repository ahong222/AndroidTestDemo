package com.ifnoif.androidtestdemo.ORM;

import io.realm.RealmObject;

/**
 * Created by apple on 17-2-16.
 */

public class DBInfo extends RealmObject {
    public String id;
    public String name;
    public int count=0;
    public String column_v3;
}
