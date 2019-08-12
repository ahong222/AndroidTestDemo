package com.ifnoif.androidtestdemo.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ifnoif.androidtestdemo.BaseFragment
import com.ifnoif.androidtestdemo.R

/**
 * Created by shen on 17/4/10.
 */

class KotlinFragment : BaseFragment() {
    var view1: View? = null;

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view1 = inflater!!.inflate(R.layout.kotlin_fragment, container, false);
        init();
        return view1;
    }

    fun init() {
        var button:View? = view1?.findViewById(R.id.kotlinButton)
        button?.setOnClickListener { v ->
            run {
            }
        }
    }

}