package com.example.administrator.phonehelper.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.widget.Toast;

/**
 * 内存及存储空间信息获取
 *
 * 1. 自带空间不大, 支持扩展卡 TF 2. 自带空间不大, 内置空间16G,32G, 不支持扩展TF 3. 自带空间不大, 内置空间16G,32G,
 * 支持扩展TF
 *
 * 1. 自带空间不大, 内置空间16G,32G 2. 自带空间不大, 内置空间16G,32G, 支持扩展TF 3. 自带空间不大2G
 *
 */

public class MemoryManager {
    // --------------------------------------------------------------------
    /** 设备自身存储全部大小单位B */
    public static long getSelfSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long blockCount = stat.getBlockCount();
        long dataFileSize = blockCount * blockSize;

        path = Environment.getDownloadCacheDirectory();
        stat = new StatFs(path.getPath());
        blockSize = stat.getBlockSize();
        blockCount = stat.getBlockCount();
        long cacheFileSize = blockCount * blockSize;

        path = Environment.getRootDirectory();
        stat = new StatFs(path.getPath());
        blockSize = stat.getBlockSize();
        blockCount = stat.getBlockCount();
        long rootFileSize = blockCount * blockSize;

        return dataFileSize + cacheFileSize + rootFileSize;
    }

    /** 设备自身存储空闲有效空间大小单位B */
    public static long getSelfAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long blockCount = stat.getAvailableBlocks();
        long dataFileSize = blockCount * blockSize;

        path = Environment.getDownloadCacheDirectory();
        stat = new StatFs(path.getPath());
        blockSize = stat.getBlockSize();
        blockCount = stat.getAvailableBlocks();
        long cacheFileSize = blockCount * blockSize;

        path = Environment.getRootDirectory();
        stat = new StatFs(path.getPath());
        blockSize = stat.getBlockSize();
        blockCount = stat.getAvailableBlocks();
        long rootFileSize = blockCount * blockSize;
        return dataFileSize + cacheFileSize + rootFileSize;
    }



    // --------------------------------------------------------------------
    /** 获取手机外置sdcard路径, 为null表示无外置TF卡 */
    public static String getPhoneOutTFPath() {

//		Map<String, String> map = System.getenv();
//		//LogUtil.i("map"+map);
//		if (map.containsKey("EXTERNAL_STORAGE2")) {
//			String paths = map.get("EXTERNAL_STORAGE2");
//			String path[] = paths.split(":");
//			if (path == null || path.length <= 0) {
//				return null;
//			}
//			return path[0];
//		}SECONDARY_STORAGE
//		return null;

        Map<String, String> map = System.getenv();

        if (map.containsKey("EXTERNAL_STORAGE")) {
            //Environment.getExternalStorageDirectory  内置ka
            String paths = map.get("EXTERNAL_STORAGE");
            // /storage/extSdCard
            String path[] = paths.split(":");
            if (path == null || path.length <= 0) {
                return null;
            }
            return path[0];
        }
        return null;
    }

    /** 获取手机内置sdcard路径, 为null表示无内置TF卡 */
    public static String getPhoneInTFPath() {
        String sdcardState = Environment.getExternalStorageState();
        if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /** 获取手机存储卡大小(有外置获取的是外置TF卡大小, 无外置获取的是内置大小) */
    public static long getSDCardSize() {
        String tfPath = "";
        // 获取外置TF卡路径 - 返回外置TF卡大小
        tfPath = getPhoneOutTFPath();
        if (tfPath != null) {
            File path = new File(tfPath);
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long blockCount = stat.getBlockCount();
            return blockCount * blockSize;
        }
        // 没有获取到了外置TF卡路径 - 返回内置TF卡大小
        tfPath = getPhoneInTFPath();
        if (tfPath != null) {
            File path = new File(tfPath);
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long blockCount = stat.getBlockCount();
            return blockCount * blockSize;
        }
        return 0;
    }

    public static long getSDCardAvailableSize() {
        String tfPath = "";
        // 获取外置TF卡路径 - 返回外置TF卡大小
        tfPath = getPhoneOutTFPath();
        if (tfPath != null) {
            File path = new File(tfPath);
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long blockCount = stat.getAvailableBlocks();
            return blockCount * blockSize;
        }
        // 没有获取到了外置TF卡路径 - 返回内置TF卡大小
        tfPath = getPhoneInTFPath();
        if (tfPath != null) {
            File path = new File(tfPath);
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long blockCount = stat.getAvailableBlocks();
            return blockCount * blockSize;
        }
        return 0;
    }

    // --------------------------------------------------------------------
    /** 设备空闲运行内存 单位B */
    public static long getPhoneFreeRamMemory(Context context) {
        MemoryInfo info = new MemoryInfo();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.getMemoryInfo(info);
        return info.availMem;
    }

    /** 设备完整运行内存 单位B */
    public static long getPhoneTotalRamMemory() {
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split("\\s+");
            return Long.valueOf(array[1]) * 1024; // 原为kb, 转为b
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
