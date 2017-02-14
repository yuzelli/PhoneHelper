package com.example.administrator.phonehelper.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.beans.PhoneManagerInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/29.
 */
public class GetPhoneSystemInfo {
    private Activity activity;

    public GetPhoneSystemInfo(Activity activity) {
        this.activity = activity;
    }

    public List<PhoneManagerInfo> getDeviceInfo() {
        List<PhoneManagerInfo> list = new ArrayList<>();
        PhoneManagerInfo info;
//       手机型号
        String model = android.os.Build.MODEL;
//        系统版本
        String release = android.os.Build.VERSION.RELEASE;
        info = new PhoneManagerInfo(R.mipmap.setting_info_icon_version,"设备名称：", model, "系统版本：", release);
        list.add(info);

//        获得机身内存总大小
        String romTotalSize = getRomTotalSize();
//        获得机身可用内存
        String romAvailableSize = getRomAvailableSize();
        info = new PhoneManagerInfo(R.mipmap.setting_info_icon_space,"全部运行内存：", romTotalSize, "剩余运行内存：", romAvailableSize);
        list.add(info);

//        获取cpu名称
        String CPUName = getCpuName();
//          获取cpu个数
        int CPUNumber = getNumCores();
        info = new PhoneManagerInfo(R.mipmap.setting_info_icon_cpu,"CPU名称：", CPUName, "CPU个数：", CPUNumber + "");
        list.add(info);

//        获取手机分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

//        获取相机分辨率
        Camera camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        //获取预览的各种分辨率
        // List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
//       获取摄像头支持的各种分辨率
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        info = new PhoneManagerInfo(R.mipmap.setting_info_icon_camera,"获取手机分辨率：", widthPixels + "" + "*" + heightPixels + "",
                "获取相机分辨率：", supportedPictureSizes.get(0).width + "" + "*" + supportedPictureSizes.get(0).height + "");
        list.add(info);


//        获取基带版本
        String baseBand = getBaseBand();
//          获取是否root
        String root = null;
        if (isRoot()){
            root = "是";
        }else {
            root = "否";
        }
        baseBand = baseBand.substring(baseBand.indexOf(',')+1);
        info = new PhoneManagerInfo(R.mipmap.setting_info_icon_root,"基带版本：", baseBand, "是否root：",root);
        list.add(info);


        return list;
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public String getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(activity, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public String getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(activity, blockSize * availableBlocks);
    }

    /* 获取CPU名字 */
    private static String getCpuName() {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return null;
    }
    //CPU个数

    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());

            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Print exception
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }

    //获取基带版本
    private String getBaseBand() {
        Object result = null;
        try {
            Class cl = Class.forName("android.os.SystemProperties");

            Object invoker = cl.newInstance();

            Method m = cl.getMethod("get", new Class[]{String.class, String.class});

            result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (String) result;
    }

    /**
     * 判断当前手机是否有ROOT权限
     * @return
     */
    private boolean isRoot(){
        boolean bool = false;

        try{
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
                bool = false;
            } else {
                bool = true;
            }

        } catch (Exception e) {

        }
        return bool;
    }
}
