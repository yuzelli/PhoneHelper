package com.example.administrator.phonehelper.utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
/**
 * Created by Administrator on 2016/12/6.
 */

public class CleanUtil
{
    //关闭当前运行的进程
    public Object [] killRunnintAppInfo(Context context)
    {

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = mActivityManager.getRunningAppProcesses();
        int appSize = getRunningTasksSize(context);
        long memory = getUesdMemory(context);
        for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess)
        {
            mActivityManager.killBackgroundProcesses(amProcess.processName);
            Log.d("AAA", "杀掉的进程："+amProcess.processName);
        }
        appSize = Math.abs(appSize -getRunningTasksSize(context));
        memory = Math.abs(memory -getUesdMemory(context));
        return getRecycleMemoryInfo(context,appSize,memory);
    }

    //强制关闭进程
    private  void forceKillApp(ActivityManager am, String packageName)
    {
        Method forceStopPackage = null;
        try
        {
            forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, packageName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 将要传出去的数据
     * 杀了多少进程
     * 释放多少M内存
     * 当前内存百分比
     * @param context
     * @param appSize
     * @param memory
     * @return
     */
    private Object [] getRecycleMemoryInfo(Context context,int appSize,long memory) {
        Object[] pram=new Object[]{0,0,0};;
        if(memory>=0)
        {
            pram[0] = appSize;
            pram[1] = (memory/1024.0);
            pram[2] = getUesdMemoryRate(context);
        }
        return pram;
    }

    private int getRunningTasksSize(Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        return am.getRunningAppProcesses().size();
    }

    /**
     * 得到设备的所有RAM
     * @return 返回所有内存大小，单位：kb
     */
    public int getAllMemory() {
        String filePath = "/proc/meminfo";
        int ram = 0;
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        try {
            fr = new FileReader(filePath);
            localBufferedReader = new BufferedReader(fr, 8192);
            String line = localBufferedReader.readLine();
            int a = line.length() - 3;
            int b = line.indexOf(' ');
            String str = line.substring(b, a);
            while (str.substring(0, 1).equals(" ")) {
                str = str.substring(1, str.length());
            }
            ram = Integer.parseInt(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
                localBufferedReader.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        return ram;
    }

    /**
     * 得到设备的可用RAM
     * @return 返回所有内存大小，单位：kb
     */
    public long getAvailMemory(Context context)
    {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / 1024;
    }

    /**
     * 得到设备的已用RAM
     * @return 返回所有内存大小，单位：kb
     */
    public long getUesdMemory(Context context)
    {
        return getAllMemory() - getAvailMemory(context);
    }

    public int getUesdMemoryRate(Context context)
    {
        return (int) (getUesdMemory(context)*100/getAvailMemory(context));
    }

    /**
     * 判断是否在白名单之内
     * @param pkg
     * @param list
     * @return
     */
    private boolean isInWhiteList(String pkg,List<String> list)
    {
        boolean inOrNot = false;
        if(list!=null)
        {
            for(int i=0;i<list.size();i++)
            {
                if(pkg.equals(list.get(i)))
                {
                    inOrNot = true;
                    break;
                }
            }
        }

        return inOrNot;
    }

}
