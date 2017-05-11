package com.ifnoif.androidtestdemo

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mmap_layout.view.*
import java.io.*
import java.nio.channels.FileChannel

/**
 * Created by shen on 17/5/10.
 * 测试结果：
 * 普通inputStream读，mmap写比randomAccess写要慢，why？
 * mmap直接写1w次string，时间比randomAccess少一倍
 */
class MMapFragment : BaseFragment {
    var MAX_TIMES = 2
    var log: String = "this is a test log, 这是测试log,this is a test log, 这是测试log,this is a test log, 这是测试log"
    var writeLog = false
    var writeLogCount = 10000

    constructor() {

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.mmap_layout, container, false)
        init(view);
        return view;
    }

    fun init(view: View) {
        view.writeFileTest.setOnCheckedChangeListener { buttonView, isChecked -> writeLog = !isChecked }
        writeLog = !view.writeFileTest.isChecked

        view.mapWriter.setOnClickListener { writeByMMap() }
        view.randomWriter.setOnClickListener { writeByRandomAccess() }
        view.normalWriter.setOnClickListener { writeByNormal() }
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

    var byteBuffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var count = 0;
}