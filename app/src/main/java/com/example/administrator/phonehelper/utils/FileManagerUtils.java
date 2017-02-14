package com.example.administrator.phonehelper.utils;

import com.example.administrator.phonehelper.beans.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class FileManagerUtils {

    /** 所有文件的KEY */
    public static final String KEY_ANY = "any";
    /** 图像文件等的KEY */
    public static final String KEY_IMAGE = "image";
    public static final String KEY_TXT = "txt";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_AUDIO = "audio";
    public static final String KEY_ZIP = "zip";
    public static final String KEY_APK = "apk";

    //{ (image,Arraylist<Fileinfo>),(),(),(),(),(),()         }
    //Arraylist<File>     a.jpg  b.png  c.jpg
    /** 用来保存搜索到的文件列表文件实体对象 */
    private static HashMap<String, ArrayList<FileInfo>> fileListMap = new HashMap<String, ArrayList<FileInfo>>();
    //{(image,allsize)()....}
    /** 用来保存搜索到的不同类型文件列表大小 */
    private static HashMap<String, Long> fileSizeMap = new HashMap<String, Long>();
    /** 是否终止搜索 */
    private static boolean isStopSearch;

    static {
        resetData();
    }

    /** 清空重置数据 */
    public static void resetData() {
        isStopSearch = false;
        // 重置各分类文件列表文件总大小
        fileSizeMap.clear();
        fileSizeMap.put(KEY_ANY, 0L);
        fileSizeMap.put(KEY_IMAGE, 0L);
        fileSizeMap.put(KEY_TXT, 0L);
        fileSizeMap.put(KEY_VIDEO, 0L);
        fileSizeMap.put(KEY_AUDIO, 0L);
        fileSizeMap.put(KEY_ZIP, 0L);
        fileSizeMap.put(KEY_APK, 0L);
        // 重置各分类文件列表
        fileListMap.clear();
        fileListMap.put(KEY_ANY, new ArrayList<FileInfo>());
        fileListMap.put(KEY_IMAGE, new ArrayList<FileInfo>());
        fileListMap.put(KEY_TXT, new ArrayList<FileInfo>());
        fileListMap.put(KEY_VIDEO, new ArrayList<FileInfo>());
        fileListMap.put(KEY_AUDIO, new ArrayList<FileInfo>());
        fileListMap.put(KEY_ZIP, new ArrayList<FileInfo>());
        fileListMap.put(KEY_APK, new ArrayList<FileInfo>());
        System.gc();
    }

    /** 获取搜索到的所有文件分类Map,可根据不同的key来获取到不同类型的文件列表 */
    public static HashMap<String, ArrayList<FileInfo>> getFileListMap() {
        return fileListMap;
    }
    //{}

    /** 获取搜索到的所有文件分类大小Map,可根据不同的key来获取到不同类型的文件列表内文件总大小 */
    public static HashMap<String, Long> getFileSizeMap() {
        return fileSizeMap;
    }

    /** 设置是否终止搜索 */
    public static void setStopSearch(boolean isStopSearch) {
        FileManagerUtils.isStopSearch = isStopSearch;
    }

    /** 向文件分类Map内添加分类文件 */
    public static void putFile(String key, FileInfo fileInfo) {
        fileListMap.get(key).add(fileInfo);
        // 重新计算此分类文件总大小
        long originalSize = fileSizeMap.get(key).longValue();
//		Log.d("azy", key + "原大小: " + originalSize);
        long currentSize = originalSize + fileInfo.getFile().length();
        fileSizeMap.put(key, currentSize);
//		Log.d("azy", key + "现大小: " + currentSize);
    }

    /** 移除文件分类Map内指定文件实体 */
    public static void removeFile(long fileSize, FileInfo fileInfo) {
        final String key = fileInfo.getFileType();
        fileListMap.get(key).remove(fileInfo);
        // 重新计算此分类文件总大小
        long originalSize = fileSizeMap.get(key).longValue();
//		Log.d("azy", key + "原大小: " + originalSize);
        long currentSize = originalSize - fileSize;
        fileSizeMap.put(key, currentSize);
//		Log.d("azy", key + "现大小: " + currentSize);
    }

    /**
     * 搜索手机上内置及外置卡上的文件
     *
     * @param callback
     *            搜索过程和结果的监听
     */
    public static void fileSearchFromSDCard(FileSearchCallback callback) {
        String inPath = MemoryManager.getPhoneInTFPath();
        String outPath = MemoryManager.getPhoneOutTFPath();

        if (inPath != null) {
            File inFile = new File(inPath);
            fileSearch(inFile, callback, true);
        } else {
            // 手机没有存储卡
            if (callback != null) {
                // 异常结束
                callback.onSearchEnd(true, null);
            }
        }
//		// 有外置卡 -> 先搜内置卡,再搜外置卡
//		if (outPath != null) {
//			// 先搜索inFile, 搜索结束后不用回调搜索结束 false
//			// 再搜索outFile, 搜索结束后需要回调搜索结束 true
//			if (inPath != null) {
//
//				File inFile = new File(inPath);
//				fileSearch(inFile, callback, false);
//			}
//			File outFile = new File(outPath);
//			fileSearch(outFile, callback, true);
//		}
//		// 无外置卡 -> 搜内置卡
//		else {
//			// 搜索outFile, 搜索结束后需要回调搜索结束 true
//			if (inPath != null) {
//				File inFile = new File(inPath);
//				fileSearch(inFile, callback, true);
//			} else {
//				// 手机没有存储卡
//				if (callback != null) {
//					// 异常结束
//					callback.onSearchEnd(true, null);
//				}
//			}
//		}
//

    }


    /**
     * 搜索指定目标目录中的文件
     *
     * @param targetFile
     *            搜索目标文件
     * @param callback
     *            搜索过程和结果的监听
     * @param hasNeedEnd
     *            是否需要结束回调(当依次搜索多个目录时,只有搜索最后一个目录结束时才去回调搜索结束方法 )
     * */
    private static void fileSearch(File targetFile, FileSearchCallback callback, boolean hasNeedEnd) {
        try {
            fileSearch(true, targetFile, callback, hasNeedEnd);
        }
        catch (Exception e) {

            setStopSearch(false);
        }
    }


    /**
     * 文件搜索方法(递归)
     *
     * @param isRootFile
     *            是否为搜索起始根目录 (因为是递归搜索操作,所以只有在是为起始根目录时的方法结束才是搜索的结束)
     * @param targetFile
     *            搜索目标文件
     * @param callback
     *            搜索过程和结果的监听
     * @param hasNeedEnd
     *            是否需要结束回调(当依次搜索多个目录时起作用)
     *
     * @throws Exception
     *             搜索异常，终止搜索
     */
    private static void fileSearch(boolean isRootFile, File targetFile, FileSearchCallback callback, boolean hasNeedEnd) throws Exception {
        // 终止搜索
        if (isStopSearch) {
            if (callback != null) {
                callback.onSearchEnd(true, targetFile);// 异常结束
            }
            throw new Exception("终止搜索");
        }
        // 不能读的文件不操作
        if (targetFile == null || !targetFile.canRead()) {
            return;
        }
        // 是文件
        if (targetFile.isFile()) {
            // 每当找到一个文件 ,去分类此文件,及去做接口回调处理
            switchFile(targetFile, callback);
        }
        // 是目录
        else {
            // 拿到此目录下的文件列表
            File[] files = targetFile.listFiles();
            if (files != null) {
                for (File tmpFile : files) {
                    // 递归操作
                    fileSearch(false, tmpFile, callback, hasNeedEnd);
                }
            }
        }
        // 回调一次 -- 搜索完毕(根文件位置+需反馈回调结束+有接口)
        if (isRootFile && hasNeedEnd && callback != null) {
            callback.onSearchEnd(false, targetFile);
        }
    }


    /** 每当找到一个文件时来分类此文件及做对应回调处理 */
    private static void switchFile(File targetFile, FileSearchCallback callback) {
        // 取出此文件所对应的图标以及文件类型
        String[] fileIconAndType = FileTypeManager.getFileIconAndTypeName(targetFile);
        String iconName = fileIconAndType[0]; // 图像名称
        String typeName = fileIconAndType[1]; // 分类的类型
        String openType = FileTypeManager.getMIMEType(targetFile);
        // 实例文件列表 文件实现对象

        FileInfo fileInfo = new FileInfo(targetFile, iconName, openType, typeName);
        // 添加到用来保存任意文件的集合内
        putFile(KEY_ANY, fileInfo);
        if (callback != null) {
            callback.onSearchIng(KEY_ANY);
        }
        // 根据分类来做不同的处理 , 回调一次 -- 每找到一个文件
        if (!typeName.equals(KEY_ANY)) {
            putFile(typeName, fileInfo);
            if (callback != null) {
                callback.onSearchIng(typeName);
            }
        }
    }





    /** 文件搜索监听 */
    public static interface FileSearchCallback {
        /**
         * 在搜索过程中将来回调的方法(每搜索到一个回调一次)
         *
         * @param key
         *            此文件的类型, 类型为这几个中的一种 {@link #KEY_ANY}任意类型,{@link #KEY_IMAGE}
         *            图像类型
         */
        void onSearchIng(final String key);

        /**
         * 当搜索结束后将来调用的方法
         *
         * @param isExceptionEnd
         *            是否为异常结束
         * @param targetFile
         *            目标文件(表明是搜索谁结束了)
         */
        void onSearchEnd(final boolean isExceptionEnd, final File targetFile);
    }

    /** 文件删除 */
    public static void delFile(File file) {
        // 文件 - 直接删除
        if (!file.isDirectory()) {
            file.delete();
        }
        // 文件夹
        else {
            // 拿到此文件夹下文件列表
            File files[] = file.listFiles();
            if (file != null) {
                // 循环操作此文件夹下文件列表(递归删除下面所有文件)
                for (int i = 0; i < files.length; i++) {
                    delFile(files[i]); // 递归
                }
                file.delete(); // 删除此文件夹
            }
        }
    }

    /** 取得文件或文件夹大小 */
    public static long getFileSize(File file) {
        long size = 0;
        if (!file.isDirectory()) { // 文件
            return file.length();
        }
        File files[] = file.listFiles(); // 文件夹（递归）
        if (file != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    size = size + getFileSize(files[i]);
                } else {
                    size = size + files[i].length();
                }
            }
        }
        return size;
    }
}
