package com.ifnoif.androidtestdemo.hook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ifnoif.androidtestdemo.BaseFragment
import com.ifnoif.androidtestdemo.R

class HookFragment:BaseFragment() {
    companion object {
        var TAG1 = "HookFragment"
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View? = inflater?.inflate(R.layout.fragment_hook_activity, container, false)

        initHook()

        val startActivityView: View? = view?.findViewById(R.id.start_hook_activity)
        startActivityView?.setOnClickListener(View.OnClickListener {
            //RealActivity1 没有在manifest中注册
            startActivity(Intent(this.context, RealActivity1::class.java))

        })


        val startActivityViewForResult: View? = view?.findViewById(R.id.start_hook_activity_for_result)
        startActivityViewForResult?.setOnClickListener(View.OnClickListener {
            //RealActivity1 没有在manifest中注册
            startActivityForResult(Intent(this.context, RealActivity1::class.java), 100)
        })
        val startServiceView: View? = view?.findViewById(R.id.start_hook_service)
        startServiceView?.setOnClickListener(View.OnClickListener {
            //RealActivity1 没有在manifest中注册
            this.context.startService(Intent(this.context, PluginService::class.java))
        })


        return view
    }

    fun initHook() {
        FakeActivity.initHook(this.context)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG1, "onActivityResult requestCode:" + requestCode + " result:" + resultCode)
    }

}