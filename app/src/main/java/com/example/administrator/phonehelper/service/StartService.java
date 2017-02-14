package com.example.administrator.phonehelper.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.administrator.phonehelper.R;
import com.example.administrator.phonehelper.activitys.MainActivity;

/**
 * Created by Administrator on 2016/11/30.
 */
public class StartService extends Service {
    // 获取消息线程
    private MessageThread messageThread = null;

    // 点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    // 通知栏消息
    private int messageNotificationID = 1000;
    private Notification messageNotification = null;
    private NotificationManager messageNotificatioManager = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 初始化
        messageNotification = new Notification();
        messageNotification.icon = R.mipmap.icon_png;
        messageNotification.tickerText = "新消息";
        messageNotification.defaults = Notification.DEFAULT_SOUND;
        messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        messageIntent = new Intent(this, MainActivity.class);
        messagePendingIntent = PendingIntent.getActivity(this, 0,
                messageIntent, 0);

        // 开启线程
        messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 从服务器端获取消息
     *
     */
    class MessageThread extends Thread {
        // 设置是否循环推送
        public boolean isRunning = true;

        public void run() {
            // while (isRunning) {
            try {
                // 间隔时间
                Thread.sleep(1000);
                // 获取服务器消息
                String serverMessage = getServerMessage();
                if (serverMessage != null && !"".equals(serverMessage)) {
                    // 更新通知栏
                    messageNotification  = new Notification.Builder(getApplicationContext())
                            .setContentTitle("phoneHepler") //设置下拉后通知标题
                            .setContentText("开机发送一条通知") //设置下拉后出现的内容
                            .setSmallIcon(R.mipmap.icon_png)
                            .setContentIntent(messagePendingIntent)
                            .build();
                    //startForeground(1,messageNotification);
                    if(getSharedPreferences("phoneHelperShared", MODE_PRIVATE).getBoolean("isSendNotification",false)){
                        messageNotificatioManager.notify(messageNotificationID,
                                messageNotification);
                        // 每次通知完，通知ID递增一下，避免消息覆盖掉
                        messageNotificationID++;
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 模拟发送消息
     *
     * @return 返回服务器要推送的消息，否则如果为空的话，不推送
     */
    public String getServerMessage() {
        return "NEWS!";
    }
}
