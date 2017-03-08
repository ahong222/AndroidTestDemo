package com.ifnoif.androidtestdemo.account;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


public class StubProvider extends ContentProvider {
    private static final String TAG = "StubProvider";

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Log.d(TAG, "query");
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.d(TAG, "insert");
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        Log.d(TAG, "delete");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        Log.d(TAG, "update");
        return 0;
    }
}
