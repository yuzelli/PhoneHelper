package com.example.administrator.phonehelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.administrator.phonehelper.widgets.MoveProgressBar;
import com.example.administrator.phonehelper.widgets.MoveTextView;

/**
 * Created by Administrator on 2016/12/5.
 */

public class BatteryReceiver extends BroadcastReceiver {
    private MoveTextView moveTextView;
    private MoveProgressBar moveProgressBar;

    public BatteryReceiver(MoveTextView moveTextView, MoveProgressBar moveProgressBar) {
        this.moveTextView = moveTextView;
        this.moveProgressBar = moveProgressBar;
    }

    static int  percent ;
    @Override
    public void onReceive(Context context, Intent intent) {
        int current = intent.getExtras().getInt("level");// 获得当前电量
        int total = intent.getExtras().getInt("scale");// 获得总电量
        percent = current * 100 / total;
        Toast.makeText(context,percent+"",Toast.LENGTH_SHORT).show();
        updateView();
    }

    public int updateView() {
        moveTextView.setTitleContent(percent);
        moveProgressBar.setProgressContent(percent);
        return percent;
    }
}
