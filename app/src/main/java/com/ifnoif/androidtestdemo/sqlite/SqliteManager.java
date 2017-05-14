package com.ifnoif.androidtestdemo.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ifnoif.androidtestdemo.MyApplication;

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

    public int queryLogCount() {
        int result = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select count(*) as count from test", null);

            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getInt(cursor.getColumnIndex("count"));
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
