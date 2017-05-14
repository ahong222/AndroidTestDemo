package com.ifnoif.androidtestdemo

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ifnoif.androidtestdemo.ORM.MyRealmLog
import com.ifnoif.androidtestdemo.sqlite.SqliteManager
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.mmap_layout.view.*
import java.io.*
import java.nio.channels.FileChannel

/**
 * Created by shen on 17/5/10.
 * 测试结果：
 * 普通inputStream读，mmap写比randomAccess写要慢，why？
 * mmap直接写1w次string，时间比randomAccess少一倍
 * realm写数据库的速度比直接写文件慢1千倍。  realm写一行数据库要4毫秒，直接写文件一行数据只用4纳秒。
 * 初始时realm写入时间是sqlite写入时间的1/3，有5000条数据后realm和sqlite写入速度差不多，1万条数据后realm所需时间是sqlte的3倍
 */
class MMapFragment : BaseFragment {
    var MAX_TIMES = 0
    var log: String = "this is a test log, 这是测试log,this is a test log, 这是测试log,this is a test log, 这是测试log"
    var writeLog = false
    var writeLogCount = 1000

    var count = 0
    var byteBuffer = ByteArray(DEFAULT_BUFFER_SIZE)

    lateinit var realmLogList: RealmResults<MyRealmLog>


    constructor() {

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.mmap_layout, container, false)
        init(view)
        initRealm()

        var time = System.currentTimeMillis();
        var logList = SqliteManager.getInstance().queryLogCount()
        Log.d("test", "log count:" + logList.size + " time:" + (System.currentTimeMillis() - time))
//        SqliteManager.getInstance().deleteAllLog()
        return view;
    }

    fun init(view: View) {
        view.writeFileTest.setOnCheckedChangeListener { buttonView, isChecked -> writeLog = !isChecked }
        writeLog = !view.writeFileTest.isChecked

        view.mapWriter.setOnClickListener { writeByMMap() }
        view.randomWriter.setOnClickListener { writeByRandomAccess() }
        view.normalWriter.setOnClickListener { writeByNormal() }
        view.realmWriter.setOnClickListener { writeByRealm() }
        view.sqliteWriter.setOnClickListener { writeBySqlite() }

//        RealmActivity.initRealm()
    }

    fun writeByMMap() {
        Thread {
            var totalTime = 0L
            for (i in 0..MAX_TIMES) {
                totalTime += realWriteByMMap()
            }

            Log.d("test", "writeByMMap total time:" + totalTime)
        }.start()

    }

    fun realWriteByMMap(): Long {
        Log.d("test", "start writeByMMap")
        var fileInputStream = getInputStream()
        var size = fileInputStream.available();
        var randomAccessFile = RandomAccessFile(File(Environment.getExternalStorageDirectory().absoluteFile, "test.mp4"), "rw")
        var mappedByteBuffer = randomAccessFile.channel.map(FileChannel.MapMode.READ_WRITE, 0, if (writeLog) (log.toByteArray().size * writeLogCount).toLong() else size.toLong())

        var time = System.currentTimeMillis()
        if (writeLog) {
            for (i in 0..writeLogCount - 1) {
                mappedByteBuffer.put(log.toByteArray());
            }
        } else {
            var writeCount = 0
            while (fileInputStream.read(byteBuffer).let { count = it;count != -1 }) {
                writeCount++
                try {
                    mappedByteBuffer.put(byteBuffer, 0, count)
                } catch (e: Exception) {
                    break;
                }
            }
            Log.d("test", "realWriteByMMap writeCount:" + writeCount)
        }

        var totalTime = System.currentTimeMillis() - time
        fileInputStream.close()

        return totalTime

    }

    fun writeByRandomAccess() {
        Thread {
            var totalTime = 0L
            for (i in 0..MAX_TIMES) {
                totalTime += realWriteByRandomAccess()
            }

            Log.d("test", "writeByRandomAccess total time:" + totalTime)
        }.start()

    }

    fun realWriteByRandomAccess(): Long {
        Log.d("test", "start realWriteByRandomAccess")
        var fileInputStream = getInputStream()
        var randomAccessFile = RandomAccessFile(File(Environment.getExternalStorageDirectory().absoluteFile, "test.mp4"), "rw")

        var time = System.currentTimeMillis()
        if (writeLog) {
            for (i in 0..writeLogCount - 1) {
                randomAccessFile.write(log.toByteArray());
            }
        } else {
            var writeCount = 0
            while (fileInputStream.read(byteBuffer).let { count = it;count != -1 }) {
                writeCount++
                try {
                    randomAccessFile.write(byteBuffer, 0, count)
                } catch (e: Exception) {
                    break;
                }
            }
            Log.d("test", "realWriteByRandomAccess writeCount:" + writeCount)
        }
        var totalTime = System.currentTimeMillis() - time
        fileInputStream.close()

        return totalTime
    }

    fun writeByNormal() {
        Thread {
            var totalTime = 0L
            for (i in 0..MAX_TIMES) {
                totalTime += realWriteByNormal()
            }

            Log.d("test", "writeByNormal total time:" + totalTime)
            showLog("writeByNormal total time:" + totalTime)
        }.start()
    }

    fun realWriteByNormal(): Long {
        Log.d("test", "start realWriteByNormal")
        var fileInputStream = getInputStream()
        var fileOutputStream = FileOutputStream(File(Environment.getExternalStorageDirectory().absoluteFile, "test.mp4"))

        var time = System.currentTimeMillis()

        if (writeLog) {
            for (i in 0..writeLogCount - 1) {
                fileOutputStream.write(log.toByteArray());
            }
        } else {
            var writeCount = 0
            while (fileInputStream.read(byteBuffer).let { count = it;count != -1 }) {
                writeCount++
                try {
                    fileOutputStream.write(byteBuffer, 0, count)
                } catch (e: Exception) {
                    break;
                }
            }
            Log.d("test", "realWriteByNormal writeCount:" + writeCount)
            showLog("realWriteByNormal writeCount:" + writeCount)
        }
        var totalTime = System.currentTimeMillis() - time
        fileInputStream.close()
        return totalTime
    }

    fun getInputStream(): InputStream {
        if (true) {
            return FileInputStream(Environment.getExternalStorageDirectory().absolutePath + "/test2.mp4")
        }
        return resources.assets.open("test.mp4")
    }

    fun writeByRealm() {
        Thread {
            var totalTime = 0L
            for (i in 0..MAX_TIMES) {
                totalTime += realWriteByRealm()
            }

            Log.d("test", "writeByRealm total time:" + totalTime)
            showLog("writeByRealm total time:" + totalTime)
        }.start()
    }

    fun realWriteByRealm(): Long {
        Log.d("test", "start realWriteByRealm")
        var time = System.currentTimeMillis()

        if (writeLog) {
            for (i in 0..writeLogCount - 1) {

                var realmLog = MyRealmLog();
                realmLog.log = log;

                var realm = Realm.getDefaultInstance()
                realm.beginTransaction()
                realm.insert(realmLog)
                realm.commitTransaction()

            }

        } else {
        }
        var totalTime = System.currentTimeMillis() - time
        return totalTime
    }

    fun writeBySqlite() {
        Thread {
            var totalTime = 0L
            for (i in 0..MAX_TIMES) {
                totalTime += realWriteBySqlite()
            }

            Log.d("test", "writeBySqlite total time:" + totalTime)
            showLog("writeBySqlite total time:" + totalTime)
        }.start()
    }

    fun realWriteBySqlite(): Long {
        Log.d("test", "start realWriteBySqlite")

        var time = System.currentTimeMillis()

        if (writeLog) {
            for (i in 0..writeLogCount - 1) {
                SqliteManager.getInstance().insertLog(log)
            }

        } else {
        }
        var totalTime = System.currentTimeMillis() - time
        return totalTime
    }

    fun showLog(log: String) {
        activity.runOnUiThread { view!!.resultLog.text = log }
    }

    fun initRealm() {
        var time = System.currentTimeMillis();
        realmLogList = Realm.getDefaultInstance().where(MyRealmLog::class.java).findAll()
        Log.d("test", "initRealm count:" + realmLogList.size + " time:" + (System.currentTimeMillis() - time))

        var deleteAll = false
        if (deleteAll) {
            Realm.getDefaultInstance().beginTransaction()
            Realm.getDefaultInstance().delete(MyRealmLog::class.java)
            Realm.getDefaultInstance().commitTransaction()
        }
    }


}