package com.ifnoif.androidtestdemo.ORM;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by shen on 17/5/12.
 */

@RealmClass
public class MyRealmLog extends RealmObject {

    public String log;
}
