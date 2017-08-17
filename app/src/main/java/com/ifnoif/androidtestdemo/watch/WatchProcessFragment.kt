package com.ifnoif.androidtestdemo.watch

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ifnoif.androidtestdemo.BaseFragment
import com.ifnoif.androidtestdemo.R
import kotlinx.android.synthetic.main.watch_process_fragment_layout.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.text.SimpleDateFormat


/**
 * Created by shen on 17/8/9.
 */
class WatchProcessFragment : BaseFragment() {

    override fun getContentResource(): Int {
        return R.layout.watch_process_fragment_layout
    }

    var mSelectedList = ArrayList<WatchInfo>()
    var mWatchResultList = ArrayList<ResultInfo>()
    var watchGap = 10 * 1000L
    var watchCount = 0;

    class ResultInfo(var process: String, var info: String) {

    }

    class WatchInfo(var process: String, var watchKilled: Boolean) {

        override fun equals(other: Any?): Boolean {
            if (other is WatchInfo) {
                return process.equals(other.process) && watchKilled == other.watchKilled
            }
            return false
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        add.setOnClickListener {
            mSelectedList.clear()
            var arrayList = getRunningProcess().map { it -> it.process }.toHashSet().sorted().toTypedArray()

            var booleans = BooleanArray(arrayList.size, { false })

            var dialog = AlertDialog.Builder(context).setMultiChoiceItems(arrayList, booleans, DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                if (!isChecked) {
                    mSelectedList.remove(WatchInfo(arrayList[which], true))
                } else {
                    mSelectedList.add(WatchInfo(arrayList[which], true))
                }
            }).create()


            dialog.setOnDismissListener {
                if (mSelectedList.isNotEmpty()) {
                    var selected = mSelectedList.reduce { total, next -> WatchInfo(total.process + ";" + next.process, true) }
                    result.setText(selected.process)
                }
                start.isEnabled = true

                startWatch()
            }
            dialog.show()
        }

        start.setOnClickListener {
            startWatch()
        }

        recycle_view.layoutManager = LinearLayoutManager(context)
        recycle_view.adapter = adapter
    }

    fun getRunningProcess(): List<ProcessInfo> {
        return getRunningApp()
    }

    class ProcessInfo(var process: String, var oom_adj: Int, var oom_score: Int, var oom_score_adj: Int) {

        override fun toString(): String {
            return "process:${process},oom_adj:${oom_adj},oom_score:${oom_score},oom_score_adj:${oom_score_adj}"
        }
    }

    fun getRunningApp(): ArrayList<ProcessInfo> {
        var result = ArrayList<ProcessInfo>()
        val files = File("/proc").listFiles()

        for (file in files) {
            if (!file.isDirectory()) {
                continue
            }
            val pid: Int

            try {
                pid = Integer.parseInt(file.getName())
            } catch (e: NumberFormatException) {
                continue
            }

            try {
                val cgroup = read(String.format("/proc/%d/cgroup", pid))
                val lines = cgroup.split("\n".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

//                var cpuaccctSubsystem: String? = null
//                var uid: Int? = null
//                for (line in lines) {
//                    var contents = line.split(":")
//                    if (contents.size >=3 && "cpuacct".equals(contents[1])) {
//                        cpuaccctSubsystem = contents[1]
//                        try {
//                            uid = Integer.parseInt(contents[2])
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                        break
//                    }
//                }
//
//                if (uid == null || cpuaccctSubsystem == null) {
//                    continue
//                }
//
//                if (!(cpuaccctSubsystem?.endsWith(Integer.toString(pid)))) {
//                    // not an application process
//                    continue
//                }
//                if (cpuSubsystem.endsWith("bg_non_interactive")) {
//                    // background policy
//                    continue
//                }

                val cmdline = read(String.format("/proc/%d/cmdline", pid))
                if (cmdline.contains("com.android.systemui")) {
                    continue
                }
//                val uid = Integer.parseInt(cpuaccctSubsystem?.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
//                        .split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].replace("uid_", ""))
//                if (uid >= 1000 && uid <= 1038) {
//                    // system process
//                    continue
//                }
//                var appId = uid - AID_APP
//                var userId = 0
//                // loop until we get the correct user id.
//                // 100000 is the offset for each user.
//
//                while (appId > AID_USER) {
//                    appId -= AID_USER
//                    userId++
//                }
//
//                if (appId < 0) {
//                    continue
//                }
                // u{user_id}_a{app_id} is used on API 17+ for multiple user
                // account support.
                // String uidName = String.format("u%d_a%d", userId, appId);
                val oomScoreAdj = File(String.format(
                        "/proc/%d/oom_score_adj", pid))
                var oom_score_adj: Int = -99
                if (oomScoreAdj.canRead()) {
                    oom_score_adj = Integer.parseInt(read(oomScoreAdj
                            .getAbsolutePath()))
//                    if (oomAdj != 0) {
//                        continue
//                    }
                }
                val oomscore = Integer.parseInt(read(String.format(
                        "/proc/%d/oom_score", pid)))
//                if (oomscore < lowestOomScore) {
//                    lowestOomScore = oomscore
//                    foregroundProcess = cmdline
//                }

                val oomadj = Integer.parseInt(read(String.format(
                        "/proc/%d/oom_adj", pid)))

                var processInfo = ProcessInfo(cmdline, oomadj, oomscore, oom_score_adj)
                result.add(processInfo)

                Log.d(TAG, "running:" + processInfo)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result;

    }

    @Throws(IOException::class)
    private fun read(path: String): String {
        val output = StringBuilder()
        val reader = BufferedReader(FileReader(path))
        output.append(reader.readLine())

        var line = reader.readLine()
        while (line != null) {
            output.append('\n').append(line)
            line = reader
                    .readLine()
        }
        reader.close()
        return output.toString().trim { it <= ' ' }// 不调用trim()，包名后会带有乱码
    }

    fun startWatch() {
        if (mSelectedList.isEmpty()) {
            return;
        }
        watchCount = 0;
        start.isEnabled = false
        mWatchResultList.clear()
        handler.removeMessages(MSG_WHATCH)
        handler.sendEmptyMessage(MSG_WHATCH)
    }

    fun check() {
        watchCount++
        count.text = "监控次数：" + watchCount
        var changed = false
        var list = getRunningProcess().map { it -> it.process }
        for (item in mSelectedList) {
            if (item.watchKilled) {
                if (!list.contains(item.process)) {
                    mWatchResultList.add(ResultInfo(item.process, simpleDateFormat.format(System.currentTimeMillis()) + " 被杀"))

                    item.watchKilled = !item.watchKilled
                    changed = true
                }
            } else {
                if (list.contains(item.process)) {
                    mWatchResultList.add(ResultInfo(item.process, simpleDateFormat.format(System.currentTimeMillis()) + " 被启动"))

                    item.watchKilled = !item.watchKilled
                    changed = true
                }
            }

        }
        if (changed) {
            adapter.notifyDataSetChanged()
        }
    }

    var MSG_WHATCH = 1
    var handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg!!.what) {
                MSG_WHATCH -> {
                    check()
                    sendEmptyMessageDelayed(MSG_WHATCH, watchGap)
                }
            }
        }
    }

    var adapter = object : RecyclerView.Adapter<MyViewHolder>() {
        override fun getItemCount(): Int {
            return mWatchResultList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            holder?.onBindView(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            var view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)

            return MyViewHolder(view)
        }
    }

    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss")

    inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var textView = (itemView?.findViewById(android.R.id.text1)) as TextView
        fun onBindView(position: Int) {
            var resultInfo = mWatchResultList[position]
            textView.text = resultInfo.process + " " + resultInfo.info
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeMessages(MSG_WHATCH)
    }
}