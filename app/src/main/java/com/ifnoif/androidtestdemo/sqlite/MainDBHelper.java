package com.ifnoif.androidtestdemo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shen on 17/5/14.
 */

public class MainDBHelper extends SQLiteOpenHelper {
    public static final int version = 1;
    public MainDBHelper(Context context){
        super(context,"sqlite_db",null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table test(_id INTEGER PRIMARY KEY AUTOINCREMENT, log String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
