package com.ifnoif.androidtestdemo.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ifnoif.androidtestdemo.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shen on 17/5/14.
 */

public class SqliteManager {
    private static SqliteManager sInstance = new SqliteManager();
    private SQLiteDatabase db;

    private SqliteManager() {
        MainDBHelper dbHelper = new MainDBHelper(MyApplication.sContext);
        db = dbHelper.getWritableDatabase();
    }

    public static SqliteManager getInstance() {
        return sInstance;
    }

    public void insertLog(String log) {
        db.execSQL("insert into test(log) values ('" + log + "')");
    }

    public void deleteAllLog(){
        db.execSQL("DELETE FROM test");
        db.execSQL("VACUUM");//清除未使用空间
    }

    public List<String> queryLogCount() {
        List<String> result = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from test", null);

            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("log");
                do {
                    result.add(cursor.getString(index));
                }while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return result;
    }
}
