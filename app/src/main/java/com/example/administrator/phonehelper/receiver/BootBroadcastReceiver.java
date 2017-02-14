package com.example.administrator.phonehelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.administrator.phonehelper.activitys.MainActivity;
import com.example.administrator.phonehelper.service.StartService;

/**
 * Created by Administrator on 2016/11/30.
 * 接受系统的广播，是否开机启动app
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
       boolean isStartActivity=  context.getSharedPreferences("phoneHelperShared", Context.MODE_PRIVATE).getBoolean("isStartActivity", false);
       if (intent.getAction().equals(ACTION)&&isStartActivity){
           //后边的XXX.class就是要启动的服务
           Intent service = new Intent(context, StartService.class);
           context.startService(service);

       }

    }
}
