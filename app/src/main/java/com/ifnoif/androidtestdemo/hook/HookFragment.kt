package com.ifnoif.androidtestdemo.hook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ifnoif.androidtestdemo.BaseFragment
import com.ifnoif.androidtestdemo.R

class HookFragment:BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View? = inflater?.inflate(R.layout.fragment_hook_activity, container, false)

        initHook()

        val startActivityView: View? = view?.findViewById(R.id.start_hook_activity)
        startActivityView?.setOnClickListener(View.OnClickListener {
            //RealActivity1 没有在manifest中注册
            startActivity(Intent(this.context, RealActivity1::class.java))
        })
        return view
    }

    fun initHook() {
        FakeActivity.initHook(this.context)
    }

}