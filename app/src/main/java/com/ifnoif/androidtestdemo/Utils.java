package com.ifnoif.androidtestdemo;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;

/**
 * @author shen create on 17/12/5.
 */

public class Utils {

    public static String getStorageLogInfo(Context context) {
        try {
            String availableSDSize = Formatter.formatFileSize(context,getSDAvailableSize());
            String totalSDSize = Formatter.formatFileSize(context,  getSDTotalSize());
            String availableDataSize = Formatter.formatFileSize(context,  getDataAvailableSize());
            String totalDataSize = Formatter.formatFileSize(context,  getDataTotalSize());

            File dbFile = context.getDatabasePath("waimaie");
            String dbFileSize = Formatter.formatFileSize(context, dbFile.length());
            return new StringBuilder().append("availableSDSize:").append(availableSDSize)
                    .append(",totalSDSize:").append(totalSDSize)
                    .append(",availableDataSize:").append(availableDataSize)
                    .append(",totalDataSize:").append(totalDataSize)
                    .append(",dbFileSize:").append(dbFileSize).toString();
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 获得sd卡总容量
     *
     * @return
     */
    public static long getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        return getTotalSize(path);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static long getDataAvailableSize() {
        File path = Environment.getDataDirectory();
        return getAvailableSize(path);
    }

    /**
     * 获得sd卡总容量
     *
     * @return
     */
    public static long getDataTotalSize() {
        File path = Environment.getDataDirectory();
        return getTotalSize(path);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        return getAvailableSize(path);
    }


    private static long getAvailableSize(File dir) {
        StatFs stat = new StatFs(dir.getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return blockSize * availableBlocks;
        } else {
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return blockSize * availableBlocks;
        }
    }

    private static long getTotalSize(File dir) {
        StatFs stat = new StatFs(dir.getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getBlockCountLong();
            return blockSize * availableBlocks;
        } else {
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getBlockCount();
            return blockSize * availableBlocks;
        }
    }
}
