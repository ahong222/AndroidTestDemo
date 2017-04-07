package com.ifnoif.androidtestdemo.kotlin

import android.content.Context
import android.widget.Toast

/**
 * Created by shen on 17/4/4.
 */


fun Context.toast(message: String, time: Int) {
    Toast.makeText(this, message, time).show();
}
