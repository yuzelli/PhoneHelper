package com.example.administrator.phonehelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.administrator.phonehelper.activitys.ApplicationListActivity;

/**
 * Created by Administrator on 2016/11/30.
 * 接收应用卸载的广播
 */
public class MonitorSysReceiver extends BroadcastReceiver  {
;
    @Override
    public void onReceive(Context context, Intent intent) {
        //接收卸载广播

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            ApplicationListActivity.notifyData();
        }

    }
}
