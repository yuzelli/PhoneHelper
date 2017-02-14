package com.example.administrator.phonehelper.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.example.administrator.phonehelper.beans.ProgressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class MyAppManager {

    private PackageManager pm;
    private ActivityManager am;
    private Context context;

    private static MyAppManager appManager;
    private MyAppManager(Context context){
        pm = context.getPackageManager();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        this.context = context;

    }
    public static MyAppManager getMyAppManager(Context context){
        if(appManager == null){
            appManager = new MyAppManager(context);
        }
        return appManager;
    }

    /**
     * 获得正在运行的数据
     */

    public List<ProgressInfo> getAllAppInfo(){
        List<ProgressInfo> list = new ArrayList<>();
//        得到所有应用程序的集合
        List<PackageInfo> allAppList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
//        得到正在运行的进程集合
        List<ActivityManager.RunningAppProcessInfo> runAppList = am.getRunningAppProcesses();
        for (PackageInfo packageInfo:allAppList){
            for (int i=0;i<runAppList.size();i++){
                if (packageInfo.packageName.equals(runAppList.get(i).processName)){
                    if (!"com.example.administrator.phonehelper".equals(packageInfo.packageName)
                            &&runAppList.get(i).importance>=ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE
                            )
                    {
                        ProgressInfo info = new ProgressInfo();
//                        获取应用图标
                        info.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));
//                        获取应用名
                        info.setAppName(packageInfo.applicationInfo.loadLabel(pm)+"");
//                        获得包名
                        info.setPackageName(packageInfo.packageName);
//                        获取进程名
                        info.setProgressName(runAppList.get(i).processName);
//                        获取版本号
                        info.setViersionCode(packageInfo.versionCode);
//                        获取版本名成
                        info.setViersionName(packageInfo.versionName);
                        //内存
                        Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{runAppList.get(i).pid});
                        Debug.MemoryInfo memoryInfo = memoryInfos[0];
                        int memory = memoryInfo.getTotalPrivateDirty()/1024;
                        info.setProgressMemory(memory);
                        info.setCheck(false);
                        list.add(info);
                    }
                }
            }
        }
        return list;
    }
}
